package backend.academy.scrapper.repository.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import backend.academy.scrapper.repository.entity.Chat;
import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.repository.entity.TrackedLink;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryTrackedLinkRepositoryTest {
    private InMemoryTrackedLinkRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTrackedLinkRepository();
    }

    @Test
    @DisplayName("save() should save tracked link and return it")
    void save_TrackedLink_ShouldSaveTrackedLinkAndReturnIt() {
        // Arrange
        TrackedLink trackedLink = createTrackedLink(null, "https://example.com", 1L);

        // Act
        TrackedLink savedLink = repository.save(trackedLink);

        // Assert
        assertThat(savedLink).isNotNull();
        assertThat(savedLink.id()).isEqualTo(1L);
        assertThat(savedLink.link().url()).isEqualTo("https://example.com");
    }

    @Test
    @DisplayName("deleteByChatIdAndUrl() should delete trackedLink")
    void deleteByChatIdAndUrl_ShouldDeleteTrackedLink() {
        // Arrange
        TrackedLink trackedLink = createTrackedLink(1L, "https://example.com", 1L);
        repository.save(trackedLink);

        // Act
        repository.deleteByChatIdAndUrl(1L, "https://example.com");

        // Assert
        Optional<TrackedLink> deletedLink = repository.findByChatIdAndUrl(1L, "https://example.com");
        assertThat(deletedLink).isEmpty();
    }

    @Test
    @DisplayName("deleteByChatId() should delete all tracked links for chat")
    void deleteByChatId_ShouldDeleteAllTrackedLinksForChat() {
        // Arrange
        TrackedLink trackedLink1 = createTrackedLink(1L, "https://example1.com", 1L);
        TrackedLink trackedLink2 = createTrackedLink(2L, "https://example2.com", 1L);
        repository.save(trackedLink1);
        repository.save(trackedLink2);

        // Act
        repository.deleteByChatId(1L);

        // Assert
        Set<TrackedLink> trackedLinks = repository.findByChatId(1L);
        assertThat(trackedLinks).isEmpty();
    }

    @Test
    @DisplayName("findByChatId() should return tracked links for chat")
    void findByChatId_ShouldReturnTrackedLinksForChat() {
        // Arrange
        TrackedLink trackedLink1 = createTrackedLink(1L, "https://example1.com", 1L);
        TrackedLink trackedLink2 = createTrackedLink(2L, "https://example2.com", 1L);
        repository.save(trackedLink1);
        repository.save(trackedLink2);

        // Act
        Set<TrackedLink> trackedLinks = repository.findByChatId(1L);

        // Assert
        assertThat(trackedLinks).hasSize(2);
        assertThat(trackedLinks).contains(trackedLink1, trackedLink2);
    }

    @Test
    @DisplayName("findByUrl() should return tracked links for url")
    void findByUrl_ShouldReturnTrackedLinksForUrl() {
        // Arrange
        TrackedLink trackedLink1 = createTrackedLink(1L, "https://example.com", 1L);
        TrackedLink trackedLink2 = createTrackedLink(2L, "https://example.com", 1L);
        repository.save(trackedLink1);
        repository.save(trackedLink2);

        // Act
        Set<TrackedLink> trackedLinks = repository.findByUrl("https://example.com");

        // Assert
        assertThat(trackedLinks).hasSize(2);
        assertThat(trackedLinks).contains(trackedLink1, trackedLink2);
    }

    @Test
    @DisplayName("findByChatIdAndUrl() should return tracked link for chat and url")
    void findByChatIdAndUrl_ShouldReturnTrackedLinkForChatAndUrl() {
        // Arrange
        TrackedLink trackedLink = createTrackedLink(1L, "https://example.com", 1L);
        repository.save(trackedLink);

        // Act
        Optional<TrackedLink> foundLink = repository.findByChatIdAndUrl(1L, "https://example.com");

        // Assert
        assertThat(foundLink)
                .isPresent()
                .hasValueSatisfying(link -> assertThat(link.link().url()).isEqualTo("https://example.com"));
    }

    @Test
    @DisplayName("findByChatIdAndUrl_ShouldReturnEmptyIfNoLinkFound")
    void findByChatIdAndUrl_ShouldReturnEmptyIfNoLinkFound() {
        // Act
        Optional<TrackedLink> foundLink = repository.findByChatIdAndUrl(1L, "https://nonexistent.com");

        // Assert
        assertThat(foundLink).isEmpty();
    }

    private TrackedLink createTrackedLink(Long id, String url, Long chatId) {
        return new TrackedLink(id, new Link(id, url, null, null), new Chat(chatId), List.of(), List.of());
    }
}
