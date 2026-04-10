package backend.academy.bot.dispatcher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.command.CommandHandler;
import backend.academy.bot.model.BotState;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.state.StateMachine;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommandDispatcherTest {

    @Mock
    private StateMachine stateMachine;

    @Mock
    private TelegramService telegramService;

    @Mock
    private CommandHandler commandHandler;

    private CommandDispatcher commandDispatcher;

    private final Long chatId = 1L;
    private final String commandKey = "/start";
    private final String commandText = "/start arg1 arg2";
    private final String unknownCommandText = "/unknown arg1 arg2";
    private final String statefulInputText = "stateful input";

    @BeforeEach
    void setUp() {
        when(commandHandler.command()).thenReturn(commandKey);

        commandDispatcher = new CommandDispatcher(stateMachine, telegramService, List.of(commandHandler));
    }

    @Test
    @DisplayName("handleUpdate() should handle command correctly")
    void handleUpdate_ShouldHandleCommandCorrectly() {
        // Arrange
        Update update = createUpdate(chatId, commandText);
        String[] args = {"arg1", "arg2"};

        // Act
        commandDispatcher.handleUpdate(update);

        // Assert
        verify(commandHandler).handle(chatId, args);
        verify(stateMachine, never()).handleStatefulInput(any(Long.class), any(String.class));
        verify(telegramService, never()).sendMessage(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("handleUpdate() should handle unknown command correctly")
    void handleUpdate_ShouldHandleUnknownCommandCorrectly() {
        // Arrange
        Update update = createUpdate(chatId, unknownCommandText);
        when(stateMachine.getState(chatId)).thenReturn(BotState.IDLE);

        // Act
        commandDispatcher.handleUpdate(update);

        // Assert
        verify(commandHandler, never()).handle(any(Long.class), any(String[].class));
        verify(stateMachine, never()).handleStatefulInput(any(Long.class), any(String.class));
        verify(telegramService).sendMessage(chatId, "Команда не найдена");
    }

    @Test
    @DisplayName("handleUpdate() should handle stateful input correctly when in IDLE state")
    void handleUpdate_ShouldHandleStatefulInputCorrectly_WhenNotInIdleState() {
        // Arrange
        Update update = createUpdate(chatId, statefulInputText);
        when(stateMachine.getState(chatId)).thenReturn(BotState.AWAITING_TRACK_LINK);

        // Act
        commandDispatcher.handleUpdate(update);

        // Assert
        verify(commandHandler, never()).handle(any(Long.class), any(String[].class));
        verify(stateMachine).handleStatefulInput(chatId, statefulInputText);
        verify(telegramService, never()).sendMessage(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("handleUpdate() should handle empty message correctly")
    void handleUpdate_ShouldHandleEmptyMessageCorrectly() {
        // Arrange
        Update update = new Update();

        // Act
        commandDispatcher.handleUpdate(update);

        // Assert
        verify(commandHandler, never()).handle(any(Long.class), any(String[].class));
        verify(stateMachine, never()).handleStatefulInput(any(Long.class), any(String.class));
        verify(telegramService, never()).sendMessage(any(Long.class), any(String.class));
    }

    private Update createUpdate(Long chatId, String text) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(chat.id()).thenReturn(chatId);
        when(message.chat()).thenReturn(chat);
        when(message.text()).thenReturn(text);
        when(update.message()).thenReturn(message);

        return update;
    }
}
