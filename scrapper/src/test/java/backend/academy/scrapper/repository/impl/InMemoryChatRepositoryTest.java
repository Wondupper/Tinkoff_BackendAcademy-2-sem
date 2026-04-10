package backend.academy.scrapper.repository.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import backend.academy.scrapper.repository.entity.Chat;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryChatRepositoryTest {
    private InMemoryChatRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryChatRepository();
    }

    @Test
    @DisplayName("save() should store chat and assign ID when ID is null")
    void save_ShouldStoreChatAndAssignId_WhenIdIsNull() {
        // Arrange
        Chat chat = new Chat(null);

        // Act
        repository.save(chat);

        // Assert
        Optional<Chat> savedChat = repository.findById(chat.id());
        assertThat(savedChat).isPresent().hasValueSatisfying(c -> assertThat(c.id())
                .isEqualTo(chat.id()));
    }

    @Test
    @DisplayName("save() should store chat with existing ID")
    void save_ShouldStoreChat_WhenIdExists() {
        // Arrange
        Chat chat = new Chat(1L);

        // Act
        repository.save(chat);

        // Assert
        Optional<Chat> savedChat = repository.findById(1L);
        assertThat(savedChat).isPresent().hasValueSatisfying(c -> assertThat(c.id())
                .isEqualTo(1L));
    }

    @Test
    @DisplayName("findById() should return empty if chat does not exist")
    void findById_ShouldReturnEmpty_WhenChatDoesNotExist() {
        // Act & Assert
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("deleteById() should remove chat if it exists")
    void deleteById_ShouldRemoveChat_WhenChatExists() {
        // Arrange
        Chat chat = new Chat(null);
        repository.save(chat);

        // Act
        repository.deleteById(chat.id());

        // Assert
        assertThat(repository.findById(chat.id())).isEmpty();
    }

    @Test
    @DisplayName("deleteById() should do nothing if chat does not exist")
    void deleteById_ShouldDoNothing_WhenChatDoesNotExist() {
        // Act
        repository.deleteById(999L);

        // Assert
        assertThat(repository.findById(999L)).isEmpty();
    }
}
