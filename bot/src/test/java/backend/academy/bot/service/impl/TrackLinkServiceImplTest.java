package backend.academy.bot.service.impl;

import static backend.academy.bot.constants.TelegramMessageConstants.ERROR_MESSAGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.bot.service.ScrapperService;
import backend.academy.bot.service.TelegramService;
import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.RemoveLinkRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrackLinkServiceImplTest {

    @Mock
    private ScrapperService scrapperService;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private TrackLinkServiceImpl trackLinkService;

    private final Long chatId = 1L;
    private final AddLinkRequest addLinkRequest =
            new AddLinkRequest("https://example.com", List.of("tag1", "tag2"), List.of("filter1", "filter2"));
    private final RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest("https://example.com");

    @Test
    @DisplayName("trackLink() should call scrapperService.addLink and telegramService.sendMessage on success")
    void trackLink_ShouldCallScrapperServiceAddLinkAndTelegramServiceSendMessage_OnSuccess() {
        // Act
        trackLinkService.trackLink(chatId, addLinkRequest);

        // Assert
        verify(scrapperService).addLink(chatId, addLinkRequest);
        verify(telegramService).sendMessage(chatId, "Теперь эта ссылка отслеживается :)");
    }

    @Test
    @DisplayName("trackLink() should call telegramService.sendMessage with error message on ServerApiException")
    void trackLink_ShouldCallTelegramServiceSendMessageWithError_OnServerApiException() {
        // Arrange
        when(scrapperService.addLink(chatId, addLinkRequest)).thenThrow(new ServerApiException("Server error"));

        // Act
        trackLinkService.trackLink(chatId, addLinkRequest);

        // Assert
        verify(scrapperService).addLink(chatId, addLinkRequest);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
    }

    @Test
    @DisplayName("trackLink() should call telegramService.sendMessage with error message on ClientApiException")
    void trackLink_ShouldCallTelegramServiceSendMessageWithError_OnClientApiException() {
        // Arrange
        when(scrapperService.addLink(chatId, addLinkRequest)).thenThrow(new ClientApiException("Client error"));

        // Act
        trackLinkService.trackLink(chatId, addLinkRequest);

        // Assert
        verify(scrapperService).addLink(chatId, addLinkRequest);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
    }

    @Test
    @DisplayName("untrackLink() should call scrapperService.removeLink and telegramService.sendMessage on success")
    void untrackLink_ShouldCallScrapperServiceRemoveLinkAndTelegramServiceSendMessage_OnSuccess() {
        // Act
        trackLinkService.untrackLink(chatId, removeLinkRequest);

        // Assert
        verify(scrapperService).removeLink(chatId, removeLinkRequest);
        verify(telegramService).sendMessage(chatId, "Ссылка удалена.");
    }

    @Test
    @DisplayName("untrackLink() should call telegramService.sendMessage with error message on ServerApiException")
    void untrackLink_ShouldCallTelegramServiceSendMessageWithError_OnServerApiException() {
        // Arrange
        when(scrapperService.removeLink(chatId, removeLinkRequest)).thenThrow(new ServerApiException("Server error"));

        // Act
        trackLinkService.untrackLink(chatId, removeLinkRequest);

        // Assert
        verify(scrapperService).removeLink(chatId, removeLinkRequest);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
    }

    @Test
    @DisplayName("untrackLink() should call telegramService.sendMessage with error message on ClientApiException")
    void untrackLink_ShouldCallTelegramServiceSendMessageWithError_OnClientApiException() {
        // Arrange
        when(scrapperService.removeLink(chatId, removeLinkRequest)).thenThrow(new ClientApiException("Client error"));

        // Act
        trackLinkService.untrackLink(chatId, removeLinkRequest);

        // Assert
        verify(scrapperService).removeLink(chatId, removeLinkRequest);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
    }
}
