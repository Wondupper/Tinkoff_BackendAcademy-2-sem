package backend.academy.scrapper.client.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "rest.client.github", ignoreUnknownFields = false)
public class GitHubRestClientProperties extends AbstractRestClientProperties {
    private String token;

    public GitHubRestClientProperties(String baseUrl, String token) {
        super(baseUrl);
        this.token = token;
    }
}
