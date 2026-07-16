package net.number33.warchest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "warchest.import")
public record ImportProperties(String batFile) {
}
