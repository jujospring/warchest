package net.number33.warchest.mlb;

import net.number33.warchest.config.MlbApiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class MlbStatsClient {
	private final RestClient restClient;
	private final MlbApiProperties props;
	
	public MlbStatsClient(RestClient restClient, MlbApiProperties props) {	
		this.restClient = restClient;
		this.props = props;
	}

	public List<MlbTeamsResponse.MlbTeam> getTeams(int season) {
		MlbTeamsResponse response = restClient.get()
			.uri("/teams?sportId={sportId}&season={season}", props.sportId(), season)
			.retrieve()
			.body(MlbTeamsResponse.class);
		return response.teams();
	}
}

