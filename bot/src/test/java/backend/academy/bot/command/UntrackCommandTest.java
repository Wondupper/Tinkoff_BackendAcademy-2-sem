package backend.academy.bot.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import backend.academy.bot.model.BotState;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.bot.state.StateMachine;
import backend.academy.common.dto.RemoveLinkRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UntrackCommandTest {

    @Mock
    private TelegramService telegramService;

    @Mock
    private TrackLinkService trackLinkService;

    @Mock
    private StateMachine stateMachine;

    @InjectMocks
    private UntrackCommand untrackCommand;

    private final Long chatId = 1L;
    private final String link = "https://example.com";

    @Test
    @DisplayName("handle() should untrack link and not change state when link is provided")
    void handle_ShouldUntrackLinkAndNotChangeState_WhenLinkIsProvided() {
        // Arrange
        String[] args = {link};

        // Act
        untrackCommand.handle(chatId, args);

        // Assert
        verify(trackLinkService).untrackLink(chatId, new RemoveLinkRequest(link));
        verify(stateMachine, never()).setState(any(), any());
        verify(telegramService, never()).sendMessage(any(), any());
    }

    @Test
    @DisplayName("handle() should prompt for link and change state to AWAITING_UNTRACK_LINK when no link is provided")
    void handle_ShouldPromptForLinkAndChangeStateToAWAITING_UNTRACK_LINK_WhenNoLinkIsProvided() {
        // Arrange
        String[] args = {};

        // Act
        untrackCommand.handle(chatId, args);

        // Assert
        verify(trackLinkService, never()).untrackLink(any(), any());
        verify(stateMachine).setState(chatId, BotState.AWAITING_UNTRACK_LINK);
        verify(telegramService).sendMessage(chatId, "Введите ссылку, которую хотите удалить:");
    }
}
