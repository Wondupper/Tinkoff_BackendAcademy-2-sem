package backend.academy.scrapper.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import backend.academy.common.dto.ApiErrorResponse;
import backend.academy.common.dto.LinkUpdate;
import backend.academy.scrapper.exception.ClientApiException;
import backend.academy.scrapper.exception.ServerApiException;
import com.github.tomakehurst.wiremock.http.Body;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
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
class BotRestClientTest {
    @Autowired
    private BotRestClient botRestClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("sendUpdate() should send post request success")
    void sendUpdate_ShouldSendPostRequestSuccess() {
        // Arrange
        stubFor(post(urlEqualTo("/updates")).willReturn(aResponse().withStatus(200)));

        LinkUpdate update = new LinkUpdate(1L, "https://example.com", "description", List.of(1L));

        // Act
        botRestClient.sendUpdate(update);

        // Assert
        verify(postRequestedFor(urlEqualTo("/updates")));
    }

    @Test
    @SneakyThrows
    @DisplayName("sendUpdate() should throw exception when client error")
    void sendUpdate_ShouldThrowException_WhenClientError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "400", "name", "message", List.of());

        stubFor(post(urlEqualTo("/updates"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act
        assertThatThrownBy(() ->
                        botRestClient.sendUpdate(new LinkUpdate(1L, "https://example.com", "description", List.of(1L))))
                .isInstanceOf(ClientApiException.class);

        // Assert
        verify(postRequestedFor(urlEqualTo("/updates")));
    }

    @Test
    @SneakyThrows
    @DisplayName("sendUpdate() should throw exception when server error")
    void sendUpdate_ShouldThrowException_WhenServerError() {
        // Arrange
        ApiErrorResponse errorResponse = new ApiErrorResponse("description", "500", "name", "message", List.of());

        stubFor(post(urlEqualTo("/updates"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withResponseBody(Body.fromJsonBytes(objectMapper.writeValueAsBytes(errorResponse)))));

        // Act
        assertThatThrownBy(() ->
                        botRestClient.sendUpdate(new LinkUpdate(1L, "https://example.com", "description", List.of(1L))))
                .isInstanceOf(ServerApiException.class);

        // Assert
        verify(postRequestedFor(urlEqualTo("/updates")));
    }
}
