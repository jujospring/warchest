package net.number33.warchest.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerSeasonRepository extends JpaRepository<PlayerSeason, Long> {

    List<PlayerSeason> findBySeason(Integer season);
}
