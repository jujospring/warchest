package net.number33.warchest.api;

import java.math.BigDecimal;

public record PlayerValue(int mlbPlayerId, String fullName, int season, String teamAbbrev, BigDecimal war, Long salaryUsd, Long dollarsPerWar) {
}
