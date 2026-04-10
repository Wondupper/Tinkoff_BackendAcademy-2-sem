package backend.academy.bot.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rest.client.scrapper", ignoreUnknownFields = false)
public record ScrapperRestClientProperties(String baseUrl) {}
