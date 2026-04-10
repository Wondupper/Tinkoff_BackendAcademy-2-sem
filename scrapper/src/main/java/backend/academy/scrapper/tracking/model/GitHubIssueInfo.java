package backend.academy.scrapper.tracking.model;

import static backend.academy.scrapper.constants.GitHubPropertyConstants.CREATED_AT_PROPERTY;
import static backend.academy.scrapper.constants.GitHubPropertyConstants.HTML_URL_PROPERTY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubIssueInfo(
        @JsonProperty(HTML_URL_PROPERTY) String url,
        @JsonProperty String title,
        @JsonProperty UserInfo user,
        @JsonProperty(CREATED_AT_PROPERTY) OffsetDateTime createdAt) {
    public record UserInfo(@JsonProperty String login, @JsonProperty(HTML_URL_PROPERTY) String url) {}
}
