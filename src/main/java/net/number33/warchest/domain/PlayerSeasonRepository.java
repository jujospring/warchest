package net.number33.warchest.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerSeasonRepository extends JpaRepository<PlayerSeason, Long> {

    List<PlayerSeason> findBySeason(Integer season);

    List<PlayerSeason> findBySeasonAndTeamAbbrev(Integer season, String teamAbbrev);

    List<PlayerSeason> findByFullNameIgnoreCaseAndSeason(String fullName, Integer season);

    List<PlayerSeason> findByFullNameIgnoreCase(String fullName);

    List<PlayerSeason> findByMlbPlayerId(Integer mlbPlayerId);

    void deleteBySeason(Integer season);
}
