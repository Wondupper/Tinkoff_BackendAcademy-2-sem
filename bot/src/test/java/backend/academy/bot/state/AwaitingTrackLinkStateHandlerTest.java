package backend.academy.bot.state;

import static backend.academy.bot.constants.StateHandlerConstants.SKIP_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TelegramService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AwaitingTrackLinkStateHandlerTest {

    @Mock
    private TelegramService telegramService;

    @Mock
    private LinkStateRepository linkStateRepository;

    @InjectMocks
    private AwaitingTrackLinkStateHandler awaitingTrackLinkStateHandler;

    private final Long chatId = 1L;
    private final String input = "https://example.com";
    private final String expectedMessage = "Введите теги: (опционально) или отправьте 'Пропустить'.";

    @Test
    @DisplayName("handle() should set tracking link and send message to enter tags")
    void handle_ShouldSetTrackingLink_SendMessageToEnterTags() {
        // Act
        BotState result = awaitingTrackLinkStateHandler.handle(chatId, input);

        // Assert
        verify(linkStateRepository).setTrackingLink(chatId, input);
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_TAGS, result);
    }

    @Test
    @DisplayName("handle() should handle empty input correctly")
    void handle_ShouldHandleEmptyInputCorrectly() {
        // Arrange
        String emptyInput = "";

        // Act
        BotState result = awaitingTrackLinkStateHandler.handle(chatId, emptyInput);

        // Assert
        verify(linkStateRepository).setTrackingLink(chatId, emptyInput);
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_TAGS, result);
    }

    @Test
    @DisplayName("handle() should handle SKIP_INPUT correctly")
    void handle_ShouldHandleSKIP_INPUTCorrectly() {
        // Arrange
        String skipInput = SKIP_INPUT;

        // Act
        BotState result = awaitingTrackLinkStateHandler.handle(chatId, skipInput);

        // Assert
        verify(linkStateRepository).setTrackingLink(chatId, skipInput);
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_TAGS, result);
    }
}
