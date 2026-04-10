package backend.academy.bot.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import backend.academy.bot.model.BotState;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.common.dto.RemoveLinkRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AwaitingUntrackLinkStateHandlerTest {

    @Mock
    private TrackLinkService trackLinkService;

    @InjectMocks
    private AwaitingUntrackLinkStateHandler awaitingUntrackLinkStateHandler;

    @Test
    @DisplayName("handle() should call untrackLink with RemoveLinkRequest and return IDLE state")
    void handle_ShouldCallUntrackLink_ReturnIdleState() {
        // Arrange
        Long chatId = 1L;
        String input = "https://example.com";
        RemoveLinkRequest expectedRequest = new RemoveLinkRequest(input);

        // Act
        BotState result = awaitingUntrackLinkStateHandler.handle(chatId, input);

        // Assert
        verify(trackLinkService).untrackLink(chatId, expectedRequest);
        assertEquals(BotState.IDLE, result);
    }
}
