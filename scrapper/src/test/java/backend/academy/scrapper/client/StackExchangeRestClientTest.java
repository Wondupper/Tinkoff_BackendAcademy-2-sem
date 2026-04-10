package backend.academy.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import backend.academy.scrapper.exception.ClientApiException;
import backend.academy.scrapper.exception.ServerApiException;
import backend.academy.scrapper.tracking.model.StackExchangeAnswerInfo;
import backend.academy.scrapper.tracking.model.StackExchangeAnswersResponse;
import backend.academy.scrapper.tracking.model.StackExchangeCommentInfo;
import backend.academy.scrapper.tracking.model.StackExchangeCommentsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
class StackExchangeRestClientTest {
    private static final Long QUESTION_ID = 12345L;
    private static final String SITE = "site";
    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now(ZoneOffset.UTC);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StackExchangeRestClient stackExchangeRestClient;

    @Test
    @SneakyThrows
    @DisplayName("getAnswers() should return answers")
    void getAnswers_ShouldReturnAnswers() {
        // Arrange
        StackExchangeAnswersResponse answers = new StackExchangeAnswersResponse(List.of(new StackExchangeAnswerInfo(
                DATE_TIME, new StackExchangeAnswerInfo.AuthorInfo("Author", "profile_url"))));

        stubFor(get(urlPathEqualTo("/questions/" + QUESTION_ID + "/answers"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsBytes(answers))
                        .withHeader("Content-Type", "application/json")));

        // Act
        StackExchangeAnswersResponse result =
                stackExchangeRestClient.getAnswers(QUESTION_ID, SITE, DATE_TIME.toEpochSecond());

        // Assert
        assertThat(result).isEqualTo(answers);
        verify(getRequestedFor(urlPathEqualTo("/questions/" + QUESTION_ID + "/answers")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getAnswers() should throw ClientApiException when client error")
    void getAnswers_ShouldThrowClientApiException_WhenClientError() {
        // Arrange
        stubFor(get(urlPathEqualTo("/questions/" + QUESTION_ID + "/answers"))
                .willReturn(aResponse().withStatus(400)));

        // Act & Assert
        assertThatThrownBy(() -> stackExchangeRestClient.getAnswers(QUESTION_ID, SITE, DATE_TIME.toEpochSecond()))
                .isInstanceOf(ClientApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/questions/" + QUESTION_ID + "/answers")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getAnswers() should throw ServerApiException when server error")
    void getAnswers_ShouldThrowServerApiException_WhenServerError() {
        // Arrange
        stubFor(get(urlPathEqualTo("/questions/" + QUESTION_ID + "/answers"))
                .willReturn(aResponse().withStatus(500)));

        // Act & Assert
        assertThatThrownBy(() -> stackExchangeRestClient.getAnswers(QUESTION_ID, SITE, DATE_TIME.toEpochSecond()))
                .isInstanceOf(ServerApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/questions/" + QUESTION_ID + "/answers")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getComments() should return comments")
    void getComments_ShouldReturnComments() {
        // Arrange
        StackExchangeCommentsResponse comments = new StackExchangeCommentsResponse(List.of(new StackExchangeCommentInfo(
                DATE_TIME, new StackExchangeCommentInfo.AuthorInfo("Author", "profile_url"))));

        stubFor(get(urlPathEqualTo("/questions/" + QUESTION_ID + "/comments"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsBytes(comments))
                        .withHeader("Content-Type", "application/json")));

        // Act
        StackExchangeCommentsResponse result =
                stackExchangeRestClient.getComments(QUESTION_ID, SITE, DATE_TIME.toEpochSecond());

        // Assert
        assertThat(result).isEqualTo(comments);
        verify(getRequestedFor(urlPathEqualTo("/questions/" + QUESTION_ID + "/comments")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getComments() should throw ClientApiException when client error")
    void getComments_ShouldThrowClientApiException_WhenClientError() {
        // Arrange
        stubFor(get(urlPathEqualTo("/questions/" + QUESTION_ID + "/comments"))
                .willReturn(aResponse().withStatus(400)));

        // Act & Assert
        assertThatThrownBy(() -> stackExchangeRestClient.getComments(QUESTION_ID, SITE, DATE_TIME.toEpochSecond()))
                .isInstanceOf(ClientApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/questions/" + QUESTION_ID + "/comments")));
    }

    @Test
    @SneakyThrows
    @DisplayName("getComments() should throw ServerApiException when server error")
    void getComments_ShouldThrowServerApiException_WhenServerError() {
        // Arrange
        stubFor(get(urlPathEqualTo("/questions/" + QUESTION_ID + "/comments"))
                .willReturn(aResponse().withStatus(500)));

        // Act & Assert
        assertThatThrownBy(() -> stackExchangeRestClient.getComments(QUESTION_ID, SITE, DATE_TIME.toEpochSecond()))
                .isInstanceOf(ServerApiException.class);

        verify(getRequestedFor(urlPathEqualTo("/questions/" + QUESTION_ID + "/comments")));
    }
}
