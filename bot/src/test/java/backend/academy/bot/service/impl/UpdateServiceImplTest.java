package backend.academy.bot.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import backend.academy.bot.service.TelegramService;
import backend.academy.common.dto.LinkUpdate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateServiceImplTest {

    @Mock
    private TelegramService telegramService;

    @InjectMocks
    private UpdateServiceImpl updateService;

    private static final LinkUpdate VALID_UPDATE =
            new LinkUpdate(1L, "https://example.com", "Description of the update", List.of(1L, 2L));
    private static final LinkUpdate INVALID_UPDATE =
            new LinkUpdate(1L, "https://example.com", "Description of the update", List.of());
    private static final LinkUpdate NULL_UPDATE =
            new LinkUpdate(1L, "https://example.com", "Description of the update", null);

    @Test
    @DisplayName("processUpdate() should send messages to all chat IDs in the update")
    void processUpdate_ShouldSendMessages_ToAllChatIds_InUpdate() {
        // Arrange
        String expectedMessage =
                String.format(UpdateServiceImpl.MESSAGE_TEMPLATE, VALID_UPDATE.url(), VALID_UPDATE.description());

        // Act
        updateService.processUpdate(VALID_UPDATE);

        // Assert
        verify(telegramService).sendMessage(1L, expectedMessage);
        verify(telegramService).sendMessage(2L, expectedMessage);
        verifyNoMoreInteractions(telegramService);
    }

    @Test
    @DisplayName("processUpdate() should throw IllegalArgumentException if chat IDs list is empty")
    void processUpdate_ShouldThrowIllegalArgumentException_IfChatIdsListIsEmpty() {
        // Act & Assert
        assertThatThrownBy(() -> updateService.processUpdate(INVALID_UPDATE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat IDs list cannot be empty");

        verifyNoInteractions(telegramService);
    }

    @Test
    @DisplayName("processUpdate() should throw IllegalArgumentException if chat IDs list is null")
    void processUpdate_ShouldThrowIllegalArgumentException_IfChatIdsListIsNull() {
        // Act & Assert
        assertThatThrownBy(() -> updateService.processUpdate(NULL_UPDATE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Chat IDs list cannot be empty");

        verifyNoInteractions(telegramService);
    }
}
