package backend.academy.bot.command;

import static backend.academy.bot.constants.TelegramMessageConstants.ERROR_MESSAGE;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.bot.service.ScrapperService;
import backend.academy.bot.service.TelegramService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StartCommandTest {

    @Mock
    private TelegramService telegramService;

    @Mock
    private ScrapperService scrapperService;

    @InjectMocks
    private StartCommand startCommand;

    private final Long chatId = 1L;

    @Test
    @DisplayName("handle() should register chat and send a success message when no exceptions are thrown")
    void handle_ShouldRegisterChatAndSendSuccessMessage_WhenNoExceptionsAreThrown() {
        // Act
        startCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).registerChat(chatId);
        verify(telegramService).sendMessage(chatId, "Вы зарегистрированы!");
    }

    @Test
    @DisplayName("handle() should send an error message when ClientApiException is thrown")
    void handle_ShouldSendMessageWithError_WhenClientApiExceptionIsThrown() {
        // Arrange
        doThrow(new ClientApiException("Client error")).when(scrapperService).registerChat(chatId);

        // Act
        startCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).registerChat(chatId);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
        verify(telegramService).sendMessage(chatId, "Вы зарегистрированы!");
    }

    @Test
    @DisplayName("handle() should send an error message when ServerApiException is thrown")
    void handle_ShouldSendMessageWithError_WhenServerApiExceptionIsThrown() {
        // Arrange
        doThrow(new ServerApiException("Server error")).when(scrapperService).registerChat(chatId);

        // Act
        startCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).registerChat(chatId);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
        verify(telegramService).sendMessage(chatId, "Вы зарегистрированы!");
    }
}
