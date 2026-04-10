package backend.academy.scrapper.tracking.model;

import static backend.academy.scrapper.constants.GitHubPropertyConstants.DATE_PROPERTY;
import static backend.academy.scrapper.constants.GitHubPropertyConstants.HTML_URL_PROPERTY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubCommitInfo(
        @JsonProperty(HTML_URL_PROPERTY) String url, @JsonProperty CommitInfo commit, @JsonProperty AuthorInfo author) {

    public record CommitInfo(@JsonProperty String message, CommiterInfo committer) {
        public record CommiterInfo(@JsonProperty(DATE_PROPERTY) OffsetDateTime createdAt) {}
    }

    public record AuthorInfo(@JsonProperty String login, @JsonProperty(HTML_URL_PROPERTY) String url) {}
}
