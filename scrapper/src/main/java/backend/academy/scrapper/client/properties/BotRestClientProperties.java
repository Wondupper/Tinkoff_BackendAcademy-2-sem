package backend.academy.scrapper.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "rest.client.bot", ignoreUnknownFields = false)
public class BotRestClientProperties extends AbstractRestClientProperties {
    public BotRestClientProperties(String baseUrl) {
        super(baseUrl);
    }
}
