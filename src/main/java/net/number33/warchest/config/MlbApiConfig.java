package net.number33.warchest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class MlbApiConfig {

	@Bean
	RestClient mlbRestClient(MlbApiProperties props) {
		return RestClient.builder()
			.baseUrl(props.baseUrl())
			.build();
	}
}

