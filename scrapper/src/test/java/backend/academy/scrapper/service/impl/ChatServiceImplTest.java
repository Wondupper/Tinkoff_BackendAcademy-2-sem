package backend.academy.scrapper.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.exception.NotFoundException;
import backend.academy.scrapper.repository.ChatRepository;
import backend.academy.scrapper.repository.TrackedLinkRepository;
import backend.academy.scrapper.repository.entity.Chat;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {
    @Mock
    private ChatRepository chatRepository;

    @Mock
    private TrackedLinkRepository trackedLinkRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    @DisplayName("registerChat() should register chat when chat does not exist")
    void registerChat_ShouldRegisterChat_WhenChatDoesNotExist() {
        // Arrange
        Long chatId = 1L;
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // Act
        chatService.registerChat(chatId);

        // Assert
        verify(chatRepository).save(new Chat(chatId));
    }

    @Test
    @DisplayName("registerChat() should skip save when chat already exists")
    void registerChat_ShouldSkipSave_WhenChatAlreadyExists() {
        // Arrange
        Long chatId = 1L;
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(new Chat(chatId)));

        // Act
        chatService.registerChat(chatId);

        // Assert
        verify(chatRepository, never()).save(any(Chat.class));
    }

    @Test
    @DisplayName("deleteChat() should delete chat when chat exists")
    void deleteChat_ShouldDeleteChat_WhenChatExists() {
        // Arrange
        Long chatId = 1L;
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(new Chat(chatId)));

        // Act
        chatService.deleteChat(chatId);

        // Assert
        verify(trackedLinkRepository).deleteByChatId(chatId);
        verify(chatRepository).deleteById(chatId);
    }

    @Test
    @DisplayName("deleteChat() should throw exception when chat not found")
    void deleteChat_ShouldThrowException_WhenChatNotFound() {
        // Arrange
        Long chatId = 1L;
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> chatService.deleteChat(chatId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Chat with id " + chatId + " not found");

        verify(trackedLinkRepository, never()).deleteByChatId(anyLong());
        verify(chatRepository, never()).deleteById(anyLong());
    }
}
