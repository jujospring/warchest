package net.number33.warchest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "warchest.mlb-api")
public record MlbApiProperties(String baseUrl, int sportId) {

}

