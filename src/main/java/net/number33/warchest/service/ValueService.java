package net.number33.warchest.service;

import net.number33.warchest.api.PlayerValue;
import net.number33.warchest.api.TeamEfficiency;
import net.number33.warchest.domain.PlayerSeason;
import net.number33.warchest.domain.PlayerSeasonRepository;
import org.hibernate.mapping.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ValueService {

    // arbitrary for now: below this, dollar/war moves fast toward infinity and stops meaning much
    private static final BigDecimal MIN_RANKABLE_WAR = new BigDecimal("0.5");

    private final PlayerSeasonRepository repository;

    public ValueService(PlayerSeasonRepository repository) {
        this.repository = repository;
    }

    public List<PlayerValue> rankBySeason(int season) {
        return repository.findBySeason(season).stream()
                .filter(ps -> ps.getWar() != null && ps.getSalaryUsd() != null)
                .filter(ps -> ps.getWar().compareTo(MIN_RANKABLE_WAR) >= 0)
                .map(ValueService::toPlayerValue)
                .sorted(Comparator.comparingLong(PlayerValue::dollarsPerWar))
                .toList();
    }

    public List<PlayerValue> albatrosses(int season) {
        return repository.findBySeason(season).stream()
                .filter(ps -> ps.getWar() != null && ps.getSalaryUsd() != null)
                .filter(ps -> ps.getWar().signum() <= 0)
                .map(ValueService::toPlayerValue)
                .sorted(Comparator.comparingLong(PlayerValue::salaryUsd).reversed())
                .toList();
    }

    public List<PlayerValue> playerValueBySeason(String fullName, int season) {
        return repository.findByFullNameIgnoreCaseAndSeason(fullName, season).stream()
                .map(ValueService::toPlayerValue).toList();
    }

    public List<PlayerValue> playerValues(String fullName) {
        return repository.findByFullNameIgnoreCase(fullName).stream()
                .map(ValueService::toPlayerValue).sorted(Comparator.comparingInt(PlayerValue::mlbPlayerId)
                        .thenComparingInt(PlayerValue::season)).toList();
    }

    public List<PlayerValue> playerData(int mlbPlayerId) {
        return repository.findByMlbPlayerId(mlbPlayerId).stream()
                .map(ValueService::toPlayerValue).sorted(Comparator.comparingInt(PlayerValue::season)).toList();
    }

    // TODO: decide on variable usage, freaking hate these long streams and would prob prefer to use variables in future
    public List<PlayerValue> teamRanks(String teamAbbrev, int season) {
        List<PlayerSeason> roster = repository.findBySeasonAndTeamAbbrev(season, teamAbbrev.toUpperCase());
        Stream<PlayerValue> rosterValues = roster.stream()
                .map(ValueService::toPlayerValue)
                .sorted(Comparator.comparing(PlayerValue::dollarsPerWar, Comparator.nullsLast(Comparator.naturalOrder())));
        return rosterValues.toList();
    }

    private static PlayerValue toPlayerValue(PlayerSeason ps) {
        Long dollarsPerWar = (ps.getWar() != null && ps.getSalaryUsd() != null && ps.getWar().compareTo(MIN_RANKABLE_WAR) >= 0)
                ? BigDecimal.valueOf(ps.getSalaryUsd())
                .divide(ps.getWar(), 0, RoundingMode.HALF_UP)
                .longValueExact() : null;
        return new PlayerValue(ps.getMlbPlayerId(), ps.getFullName(), ps.getSeason(), ps.getTeamAbbrev(), ps.getWar(), ps.getSalaryUsd(), dollarsPerWar);
    }

    public TeamEfficiency teamEfficiency(String teamAbbrev, int season) {
        List<PlayerSeason> roster = repository.findBySeasonAndTeamAbbrev(season, teamAbbrev.toUpperCase());
        BigDecimal totalWar = roster.stream()
                .map(PlayerSeason::getWar)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long totalPayroll = roster.stream()
                .map(PlayerSeason::getSalaryUsd)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
        Long dollarsPerWar = totalWar.compareTo(BigDecimal.ZERO) > 0
                ? BigDecimal.valueOf(totalPayroll).divide(totalWar, 0, RoundingMode.HALF_UP).longValueExact()
                : null;

        return new TeamEfficiency(teamAbbrev.toUpperCase(), season, roster.size(), totalWar, totalPayroll, dollarsPerWar);
    }

}
