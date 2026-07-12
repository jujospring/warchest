package net.number33.warchest.api;

import net.number33.warchest.mlb.MlbStatsClient;
import net.number33.warchest.mlb.MlbTeamsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TeamController {

	private final MlbStatsClient mlbStatsClient;

	public TeamController(MlbStatsClient mlbStatsClient) {
		this.mlbStatsClient = mlbStatsClient;
	}

	@GetMapping("/teams")
	public List<MlbTeamsResponse.MlbTeam> getTeams(@RequestParam(required = false) Integer season) {
		int resolved = (season != null) ? season : Year.now().getValue();
		return mlbStatsClient.getTeams(resolved);
	}

}

