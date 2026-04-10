package backend.academy.bot.state;

import static backend.academy.bot.constants.StateHandlerConstants.SKIP_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TelegramService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AwaitingTagsStateHandlerTest {

    @Mock
    private LinkStateRepository linkStateRepository;

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private AwaitingTagsStateHandler awaitingTagsStateHandler;

    private final Long chatId = 1L;
    private final String input = "tag1 tag2";
    private final List<String> tags = List.of("tag1", "tag2");

    @Test
    @DisplayName("handle() should set tags and send message to enter filters")
    void handle_ShouldSetTags_SendMessageToEnterFilters() {
        // Arrange
        String expectedMessage = "Введите фильтры (опционально) или отправьте 'Пропустить'.";

        // Act
        BotState result = awaitingTagsStateHandler.handle(chatId, input);

        // Assert
        verify(linkStateRepository).setTags(chatId, tags);
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_FILTERS, result);
    }

    @Test
    @DisplayName("handle() should skip setting tags if input is SKIP_INPUT")
    void handle_ShouldSkipSettingTags_IfInputIsSKIP_INPUT() {
        // Arrange
        String expectedMessage = "Введите фильтры (опционально) или отправьте 'Пропустить'.";

        // Act
        BotState result = awaitingTagsStateHandler.handle(chatId, SKIP_INPUT);

        // Assert
        verify(linkStateRepository, never()).setTags(eq(chatId), anyList());
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_FILTERS, result);
    }

    @Test
    @DisplayName("handle() should handle multiple tags correctly")
    void handle_ShouldHandleMultipleTagsCorrectly() {
        // Arrange
        String multipleTagsInput = "tag1 tag2 tag3";
        List<String> multipleTags = Arrays.asList("tag1", "tag2", "tag3");
        String expectedMessage = "Введите фильтры (опционально) или отправьте 'Пропустить'.";

        // Act
        BotState result = awaitingTagsStateHandler.handle(chatId, multipleTagsInput);

        // Assert
        verify(linkStateRepository).setTags(chatId, multipleTags);
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_FILTERS, result);
    }

    @Test
    @DisplayName("handle() should handle single tag correctly")
    void handle_ShouldHandleSingleTagCorrectly() {
        // Arrange
        String singleTagInput = "tag1";
        List<String> singleTag = List.of("tag1");
        String expectedMessage = "Введите фильтры (опционально) или отправьте 'Пропустить'.";

        // Act
        BotState result = awaitingTagsStateHandler.handle(chatId, singleTagInput);

        // Assert
        verify(linkStateRepository).setTags(chatId, singleTag);
        verify(telegramService).sendMessage(chatId, expectedMessage);
        assertEquals(BotState.AWAITING_FILTERS, result);
    }
}
