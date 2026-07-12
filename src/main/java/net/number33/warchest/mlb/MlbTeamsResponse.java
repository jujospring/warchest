package net.number33.warchest.mlb;

import java.util.List;

public record MlbTeamsResponse(List<MlbTeam> teams) {
	public record MlbTeam(int id, String name, String abbreviation, String locationName) {
	}
}

