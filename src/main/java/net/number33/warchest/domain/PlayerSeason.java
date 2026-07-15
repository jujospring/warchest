package net.number33.warchest.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "player_season",
        uniqueConstraints = @UniqueConstraint(name = "uq_player_season", columnNames = {"mlb_player_id", "season"}))

public class PlayerSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer mlbPlayerId;
    private Integer season;
    private String fullName;
    private String teamAbbrev;
    private String position;
    private BigDecimal war;
    private Long salaryUsd;
    private Instant updatedAt;

    protected PlayerSeason() {
    }

    public PlayerSeason(Integer mlbPlayerId, Integer season, String fullName) {
        this.mlbPlayerId = mlbPlayerId;
        this.season = season;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public Integer getMlbPlayerId() {
        return mlbPlayerId;
    }

    public void setMlbPlayerId(Integer mlbPlayerId) {
        this.mlbPlayerId = mlbPlayerId;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTeamAbbrev() {
        return teamAbbrev;
    }

    public void setTeamAbbrev(String teamAbbrev) {
        this.teamAbbrev = teamAbbrev;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public BigDecimal getWar() {
        return war;
    }

    public void setWar(BigDecimal war) {
        this.war = war;
    }

    public Long getSalaryUsd() {
        return salaryUsd;
    }

    public void setSalaryUsd(Long salaryUsd) {
        this.salaryUsd = salaryUsd;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    @PreUpdate
    void touch() {
        this.updatedAt = Instant.now();
    }
}
