package backend.academy.bot.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.state.StateMachine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrackCommandTest {

    @Mock
    private TelegramService telegramService;

    @Mock
    private StateMachine stateMachine;

    @Mock
    private LinkStateRepository linkStateRepository;

    @InjectMocks
    private TrackCommand trackCommand;

    private final Long chatId = 1L;

    @Test
    @DisplayName("handle() should set tracking link and change state to AWAITING_TAGS when link is provided")
    void handle_ShouldSetTrackingLinkAndChangeStateToAWAITING_TAGS_WhenLinkIsProvided() {
        // Arrange
        String link = "https://example.com";
        String[] args = {link};

        // Act
        trackCommand.handle(chatId, args);

        // Assert
        verify(linkStateRepository).setTrackingLink(chatId, link);
        verify(stateMachine).setState(chatId, BotState.AWAITING_TAGS);
        verify(telegramService).sendMessage(chatId, "Введите теги: (опционально) или отправьте 'Пропустить'.");
    }

    @Test
    @DisplayName("handle() should prompt for link and change state to AWAITING_TRACK_LINK when no link is provided")
    void handle_ShouldPromptForLinkAndChangeStateToAWAITING_TRACK_LINK_WhenNoLinkIsProvided() {
        // Arrange
        String[] args = {};

        // Act
        trackCommand.handle(chatId, args);

        // Assert
        verify(linkStateRepository, never()).setTrackingLink(any(), any());
        verify(stateMachine).setState(chatId, BotState.AWAITING_TRACK_LINK);
        verify(telegramService).sendMessage(chatId, "Введите ссылку для отслеживания:");
    }
}
