package net.number33.warchest.api;

import java.math.BigDecimal;

public record TeamEfficiency(String teamAbbrev, int season, int players, BigDecimal totalWar, long totalPayrollUsd, Long dollarsPerWar) {
}
