package backend.academy.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import backend.academy.common.dto.ApiErrorResponse;
import backend.academy.scrapper.exception.ClientApiException;
import backend.academy.scrapper.exception.ServerApiException;
import backend.academy.scrapper.tracking.model.GitHubCommitInfo;
import backend.academy.scrapper.tracking.model.GitHubIssueCommentInfo;
import backend.academy.scrapper.tracking.model.GitHubIssueInfo;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
class GitHubRestClientTest {
    private static final String OWNER = "owner";
    private static final String REPO = "repo";
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now();

    @Autowired
    private GitHubRestClient gitHubRestClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SneakyThrows
    @DisplayName("getIssues() should return issues")
    void getIssues_ShouldReturnIssues() {
        // Arrange
        List<GitHubIssueInfo> issues =
                List.of(new GitHubIssueInfo("url", "Issue title", new GitHubIssueInfo.UserInfo("user", "url"), null));

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsBytes(issues))
                        .withHeader("Content-Type", "application/json")));

        // Act
        List<GitHubIssueInfo> result = gitHubRestClient.getIssues(OWNER, REPO, DATE_TIME);

        // Assert
        assertThat(result).isEqualTo(issues);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getIssues() should throw ClientApiException when client error")
    void getIssues_ShouldThrowClientApiException_WhenClientError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "400", "name", "message", List.of());

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act & Assert
        assertThatThrownBy(() -> gitHubRestClient.getIssues(OWNER, REPO, DATE_TIME))
                .isInstanceOf(ClientApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getIssues() should throw ServerApiException when server error")
    void getIssues_ShouldThrowServerApiException_WhenServerError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "500", "name", "message", List.of());

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act & Assert
        assertThatThrownBy(() -> gitHubRestClient.getIssues(OWNER, REPO, DATE_TIME))
                .isInstanceOf(ServerApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getIssueComments() should return issue comments")
    void getIssueComments_ShouldReturnIssueComments() {
        // Arrange
        List<GitHubIssueCommentInfo> comments = List.of(new GitHubIssueCommentInfo(
                "url", "Comment body", new GitHubIssueCommentInfo.UserInfo("user", "url"), null));

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues/comments"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsBytes(comments))
                        .withHeader("Content-Type", "application/json")));

        // Act
        List<GitHubIssueCommentInfo> result = gitHubRestClient.getIssueComments(OWNER, REPO, DATE_TIME);

        // Assert
        assertThat(result).isEqualTo(comments);
        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues/comments")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getIssueComments() should throw ClientApiException when client error")
    void getIssueComments_ShouldThrowClientApiException_WhenClientError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "400", "name", "message", List.of());

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues/comments"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act & Assert
        assertThatThrownBy(() -> gitHubRestClient.getIssueComments(OWNER, REPO, DATE_TIME))
                .isInstanceOf(ClientApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues/comments")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getIssueComments() should throw ServerApiException when server error")
    void getIssueComments_ShouldThrowServerApiException_WhenServerError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "500", "name", "message", List.of());

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues/comments"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act & Assert
        assertThatThrownBy(() -> gitHubRestClient.getIssueComments(OWNER, REPO, DATE_TIME))
                .isInstanceOf(ServerApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/issues/comments")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getCommits() should return commits")
    void getCommits_ShouldReturnCommits() {
        // Arrange
        List<GitHubCommitInfo> commits = List.of(new GitHubCommitInfo(
                "url",
                new GitHubCommitInfo.CommitInfo("Commit message", null),
                new GitHubCommitInfo.AuthorInfo("author", "url")));

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/commits"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsBytes(commits))
                        .withHeader("Content-Type", "application/json")));

        // Act
        List<GitHubCommitInfo> result = gitHubRestClient.getCommits(OWNER, REPO, DATE_TIME);

        // Assert
        assertThat(result).isEqualTo(commits);
        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/commits")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getCommits() should throw ClientApiException when client error")
    void getCommits_ShouldThrowClientApiException_WhenClientError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "400", "name", "message", List.of());

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/commits"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act & Assert
        assertThatThrownBy(() -> gitHubRestClient.getCommits(OWNER, REPO, DATE_TIME))
                .isInstanceOf(ClientApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/commits")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getCommits() should throw ServerApiException when server error")
    void getCommits_ShouldThrowServerApiException_WhenServerError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "500", "name", "message", List.of());

        stubFor(get(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/commits"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act & Assert
        assertThatThrownBy(() -> gitHubRestClient.getCommits(OWNER, REPO, DATE_TIME))
                .isInstanceOf(ServerApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/repos/" + OWNER + "/" + REPO + "/commits")));
    }
}
