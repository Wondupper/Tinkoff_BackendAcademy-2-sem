package backend.academy.scrapper.client.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "rest.client.stack-exchange", ignoreUnknownFields = false)
public class StackExchangeRestClientProperties extends AbstractRestClientProperties {
    @NotEmpty
    private String key;

    @NotEmpty
    private String accessToken;

    public StackExchangeRestClientProperties(String baseUrl, String key, String accessToken) {
        super(baseUrl);
        this.key = key;
        this.accessToken = accessToken;
    }
}
