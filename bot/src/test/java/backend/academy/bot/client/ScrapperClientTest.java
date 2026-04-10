package backend.academy.bot.client;

import static backend.academy.bot.client.ScrapperClient.TG_CHAT_ID_HEADER;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
class ScrapperClientTest {
    @Autowired
    private ScrapperClient scrapperClient;

    @Test
    @DisplayName("registerChat() should send post request successfully")
    void registerChat_ShouldSendPostRequestSuccessfully() {
        // Arrange
        stubFor(post(urlEqualTo("/tg-chat/1")).willReturn(aResponse().withStatus(200)));

        // Act
        scrapperClient.registerChat(1L);

        // Assert
        verify(postRequestedFor(urlEqualTo("/tg-chat/1")));
    }

    @Test
    @DisplayName("deleteChat() should send delete request successfully")
    void deleteChat_ShouldSendDeleteRequestSuccessfully() {
        // Arrange
        stubFor(delete(urlEqualTo("/tg-chat/1")).willReturn(aResponse().withStatus(200)));

        // Act
        scrapperClient.deleteChat(1L);

        // Assert
        verify(deleteRequestedFor(urlEqualTo("/tg-chat/1")));
    }

    @Test
    @DisplayName("getLinks() should return list of links")
    void getLinks_ShouldReturnListOfLinks() {
        // Arrange
        stubFor(get(urlEqualTo("/links"))
                .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"links\":[]}")
                        .withHeader("Content-Type", "application/json")));

        // Act
        ListLinksResponse response = scrapperClient.getLinks(1L);

        // Assert
        assertThat(response.links()).isEmpty();
        verify(getRequestedFor(urlEqualTo("/links")).withHeader(TG_CHAT_ID_HEADER, equalTo("1")));
    }

    @Test
    @DisplayName("addLink() should send post request successfully")
    void addLink_ShouldSendPostRequestSuccessfully() {
        // Arrange
        LinkResponse expected = new LinkResponse(1L, "https://example.com", null, null);
        stubFor(post(urlEqualTo("/links"))
                .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\":1, \"url\":\"https://example.com\"}")
                        .withHeader("Content-Type", "application/json")));

        AddLinkRequest request = new AddLinkRequest("https://example.com", List.of(), List.of());

        // Act
        LinkResponse response = scrapperClient.addLink(1L, request);

        // Assert
        assertThat(response).isEqualTo(expected);
        verify(postRequestedFor(urlEqualTo("/links")).withHeader(TG_CHAT_ID_HEADER, equalTo("1")));
    }

    @Test
    @DisplayName("removeLink() should send delete request successfully")
    void removeLink_ShouldSendDeleteRequestSuccessfully() {
        // Arrange
        LinkResponse expected = new LinkResponse(1L, "https://example.com", null, null);
        stubFor(delete(urlEqualTo("/links"))
                .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"id\":1, \"url\":\"https://example.com\"}")
                        .withHeader("Content-Type", "application/json")));

        RemoveLinkRequest request = new RemoveLinkRequest("https://example.com");

        // Act
        LinkResponse response = scrapperClient.removeLink(1L, request);

        // Assert
        assertThat(response).isEqualTo(expected);
        verify(deleteRequestedFor(urlEqualTo("/links")).withHeader(TG_CHAT_ID_HEADER, equalTo("1")));
    }

    @Test
    @DisplayName("getLinks() should throw exception when client error")
    void getLinks_ShouldThrowException_WhenClientError() {
        // Arrange
        stubFor(get(urlEqualTo("/links")).willReturn(aResponse().withStatus(400)));

        // Act & Assert
        assertThatThrownBy(() -> scrapperClient.getLinks(1L)).isInstanceOf(ClientApiException.class);
    }

    @Test
    @DisplayName("getLinks() should throw exception when server error")
    void getLinks_ShouldThrowException_WhenServerError() {
        // Arrange
        stubFor(get(urlEqualTo("/links")).willReturn(aResponse().withStatus(500)));

        // Act & Assert
        assertThatThrownBy(() -> scrapperClient.getLinks(1L)).isInstanceOf(ServerApiException.class);
    }
}
