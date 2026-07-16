package net.number33.warchest.service;

import net.number33.warchest.config.ImportProperties;
import net.number33.warchest.domain.PlayerSeason;
import net.number33.warchest.domain.PlayerSeasonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class CsvImportService {

    private static final int COL_NAME = 0;
    private static final int COL_AGE = 1;
    private static final int COL_MLB_ID = 2;
    private static final int COL_YEAR = 4;
    private static final int COL_TEAM = 5;
    private static final int COL_STINT = 6;
    private static final int COL_WAR = 30;
    private static final int COL_SALARY = 34;
    private static final int COL_PITCHER = 35;
    private static final int COL_OPSPLUS = 46;

    private final PlayerSeasonRepository repository;
    private final ImportProperties props;

    public CsvImportService(PlayerSeasonRepository repository, ImportProperties props) {
        this.repository = repository;
        this.props = props;
    }

    private record BatRow(int mlbId, String name, int season, String team, int stint, BigDecimal war, Long salary, boolean pitcher) {
        static BatRow from(String[] f) {
            return new BatRow(
                    Integer.parseInt(f[COL_MLB_ID]),
                    f[COL_NAME],
                    Integer.parseInt(f[COL_YEAR]),
                    f[COL_TEAM],
                    Integer.parseInt(f[COL_STINT]),
                    nullableDecimal(f[COL_WAR]),
                    nullableLong(f[COL_SALARY]),
                    "Y".equals(f[COL_PITCHER])
            );
        }
    }

    private static BigDecimal nullableDecimal(String s) {
        return "NULL".equals(s) ? null : new BigDecimal(s);
    }

    private static Long nullableLong(String s) {
        return "NULL".equals(s) ? null : Long.parseLong(s);
    }

    @Transactional
    public int importBattingSeason(int season) {
        Map<Integer, BatRow> byPlayer = new HashMap<>();

        // stream strings line by line from the batFile path from props
        try (Stream<String> lines = Files.lines(Path.of(props.batFile()))) {
            // skip header, parse, filter rows to the season, merge team stints to player's mlbId
            lines.skip(1)
                    .map(line -> line.split(",", -1))
                    .filter(f -> f[COL_YEAR].equals(String.valueOf(season)) && "N".equals(f[COL_PITCHER]))
                    .map(BatRow::from)
                    .forEach(r -> byPlayer.merge(r.mlbId(), r,  CsvImportService::mergeStints));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + props.batFile(), e);
        }

        // wipe the season
        repository.deleteBySeason(season);
        // put in the fresh set of player season values
        List<PlayerSeason> entities = byPlayer.values().stream().map(CsvImportService::toEntity).toList();
        repository.saveAll(entities);
        return entities.size();
    }

    private static BatRow mergeStints(BatRow a, BatRow b) {
        BatRow later = b.stint() > a.stint() ? b : a;
        return new BatRow(
                a.mlbId(),
                a.name(),
                a.season(),
                later.team(),
                later.stint(),
                addNullable(a.war(), b.war()),
                a.salary() != null ? a.salary() : b.salary(),
                a.pitcher()
        );
    }

    private static BigDecimal addNullable(BigDecimal a, BigDecimal b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.add(b);
    }

    private static PlayerSeason toEntity(BatRow r) {
        PlayerSeason ps = new PlayerSeason(r.mlbId(), r.season(), r.name());
        ps.setTeamAbbrev(r.team());
        ps.setWar(r.war());
        ps.setSalaryUsd(r.salary());

        return ps;
    }
}
