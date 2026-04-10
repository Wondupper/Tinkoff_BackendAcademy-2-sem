package backend.academy.bot.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.client.ScrapperClient;
import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScrapperServiceImplTest {

    @Mock
    private ScrapperClient scrapperClient;

    @InjectMocks
    private ScrapperServiceImpl scrapperService;

    private final Long chatId = 1L;
    private final AddLinkRequest addLinkRequest =
            new AddLinkRequest("https://example.com", List.of("tag1", "tag2"), List.of("filter1", "filter2"));
    private final RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("https://example.com");
    private final LinkResponse linkResponse =
            new LinkResponse(1L, "https://example.com", List.of("tag1", "tag2"), List.of("filter1", "filter2"));
    private final List<LinkResponse> linkResponses = List.of(linkResponse);

    @Test
    @DisplayName("registerChat() should call scrapperClient.registerChat with the correct chatId")
    void registerChat_ShouldCallScrapperClientRegisterChat_WithCorrectChatId() {
        // Arrange
        doNothing().when(scrapperClient).registerChat(chatId);

        // Act
        scrapperService.registerChat(chatId);

        // Assert
        verify(scrapperClient).registerChat(chatId);
    }

    @Test
    @DisplayName("deleteChat() should call scrapperClient.deleteChat with the correct chatId")
    void deleteChat_ShouldCallScrapperClientDeleteChat_WithCorrectChatId() {
        // Arrange
        doNothing().when(scrapperClient).deleteChat(chatId);

        // Act
        scrapperService.deleteChat(chatId);

        // Assert
        verify(scrapperClient).deleteChat(chatId);
    }

    @Test
    @DisplayName(
            "getLinks() should call scrapperClient.getLinks with the correct chatId and return the correct list of links")
    void getLinks_ShouldCallScrapperClientGetLinks_WithCorrectChatId_AndReturnCorrectListOfLinks() {
        // Arrange
        when(scrapperClient.getLinks(chatId)).thenReturn(new ListLinksResponse(linkResponses, linkResponses.size()));

        // Act
        List<LinkResponse> result = scrapperService.getLinks(chatId);

        // Assert
        verify(scrapperClient).getLinks(chatId);
        assertEquals(linkResponses, result);
    }

    @Test
    @DisplayName(
            "addLink() should call scrapperClient.addLink with the correct chatId and addLinkRequest and return the correct link response")
    void addLink_ShouldCallScrapperClientAddLink_WithCorrectChatIdAndAddLinkRequest_AndReturnCorrectLinkResponse() {
        // Arrange
        when(scrapperClient.addLink(chatId, addLinkRequest)).thenReturn(linkResponse);

        // Act
        LinkResponse result = scrapperService.addLink(chatId, addLinkRequest);

        // Assert
        verify(scrapperClient).addLink(chatId, addLinkRequest);
        assertEquals(linkResponse, result);
    }

    @Test
    @DisplayName(
            "removeLink() should call scrapperClient.removeLink with the correct chatId and removeLinkRequest and return the correct link response")
    void
            removeLink_ShouldCallScrapperClientRemoveLink_WithCorrectChatIdAndRemoveLinkRequest_AndReturnCorrectLinkResponse() {
        // Arrange
        when(scrapperClient.removeLink(chatId, removeLinkRequest)).thenReturn(linkResponse);

        // Act
        LinkResponse result = scrapperService.removeLink(chatId, removeLinkRequest);

        // Assert
        verify(scrapperClient).removeLink(chatId, removeLinkRequest);
        assertEquals(linkResponse, result);
    }
}
