package backend.academy.bot.command;

import static backend.academy.bot.constants.TelegramMessageConstants.ERROR_MESSAGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.bot.service.ScrapperService;
import backend.academy.bot.service.TelegramService;
import backend.academy.common.dto.LinkResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListCommandTest {

    @Mock
    private TelegramService telegramService;

    @Mock
    private ScrapperService scrapperService;

    @InjectMocks
    private ListCommand listCommand;

    private final Long chatId = 1L;
    private final LinkResponse linkResponse1 = new LinkResponse(1L, "https://example.com/1", null, null);
    private final LinkResponse linkResponse2 = new LinkResponse(2L, "https://example.com/2", null, null);
    private final List<LinkResponse> linkResponses = List.of(linkResponse1, linkResponse2);

    @Test
    @DisplayName("handle() should send a message with the list of links when links are available")
    void handle_ShouldSendMessageWithListOfLinks_WhenLinksAreAvailable() {
        // Arrange
        when(scrapperService.getLinks(chatId)).thenReturn(linkResponses);

        // Act
        listCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).getLinks(chatId);
        verify(telegramService)
                .sendMessage(
                        chatId,
                        "Ваши отслеживаемые ссылки:" + System.lineSeparator() + "https://example.com/1"
                                + System.lineSeparator() + "https://example.com/2");
    }

    @Test
    @DisplayName("handle() should send a message indicating no links are being tracked when no links are available")
    void handle_ShouldSendMessageIndicatingNoLinksAreBeingTracked_WhenNoLinksAreAvailable() {
        // Arrange
        when(scrapperService.getLinks(chatId)).thenReturn(List.of());

        // Act
        listCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).getLinks(chatId);
        verify(telegramService).sendMessage(chatId, "Вы не отслеживаете ни одной ссылки.");
    }

    @Test
    @DisplayName("handle() should send an error message when ClientApiException is thrown")
    void handle_ShouldSendMessageWithError_WhenClientApiExceptionIsThrown() {
        // Arrange
        when(scrapperService.getLinks(chatId)).thenThrow(new ClientApiException("Client error"));

        // Act
        listCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).getLinks(chatId);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
    }

    @Test
    @DisplayName("handle() should send an error message when ServerApiException is thrown")
    void handle_ShouldSendMessageWithError_WhenServerApiExceptionIsThrown() {
        // Arrange
        when(scrapperService.getLinks(chatId)).thenThrow(new ServerApiException("Server error"));

        // Act
        listCommand.handle(chatId, new String[] {});

        // Assert
        verify(scrapperService).getLinks(chatId);
        verify(telegramService).sendMessage(chatId, ERROR_MESSAGE);
    }
}
