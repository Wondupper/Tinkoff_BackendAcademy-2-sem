package backend.academy.bot.repository.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import backend.academy.bot.repository.LinkStateRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryLinkStateRepositoryTest {

    private LinkStateRepository linkStateRepository;

    @BeforeEach
    void setUp() {
        linkStateRepository = new InMemoryLinkStateRepository();
    }

    @Test
    @DisplayName("setTrackingLink() should set the tracking link for a chatId")
    void setTrackingLink_ShouldSetTrackingLink_ForChatId() {
        // Arrange
        Long chatId = 1L;
        String link = "https://example.com";

        // Act
        linkStateRepository.setTrackingLink(chatId, link);

        // Assert
        assertEquals(link, linkStateRepository.getTrackingLink(chatId));
    }

    @Test
    @DisplayName("getTrackingLink() should return null if chatId is not found")
    void getTrackingLink_ShouldReturnNull_IfChatIdNotFound() {
        // Arrange
        Long chatId = 1L;

        // Act & Assert
        assertNull(linkStateRepository.getTrackingLink(chatId));
    }

    @Test
    @DisplayName("getTrackingLink() should return the correct tracking link if chatId is found")
    void getTrackingLink_ShouldReturnCorrectTrackingLink_IfChatIdFound() {
        // Arrange
        Long chatId = 1L;
        String link = "https://example.com";
        linkStateRepository.setTrackingLink(chatId, link);

        // Act & Assert
        assertEquals(link, linkStateRepository.getTrackingLink(chatId));
    }

    @Test
    @DisplayName("setTags() should set the tags for a chatId")
    void setTags_ShouldSetTags_ForChatId() {
        // Arrange
        Long chatId = 1L;
        List<String> tags = Arrays.asList("tag1", "tag2");

        // Act
        linkStateRepository.setTags(chatId, tags);

        // Assert
        assertEquals(tags, linkStateRepository.getTags(chatId));
    }

    @Test
    @DisplayName("getTags() should return null if chatId is not found")
    void getTags_ShouldReturnNull_IfChatIdNotFound() {
        // Arrange
        Long chatId = 1L;

        // Act & Assert
        assertNull(linkStateRepository.getTags(chatId));
    }

    @Test
    @DisplayName("getTags() should return the correct tags if chatId is found")
    void getTags_ShouldReturnCorrectTags_IfChatIdFound() {
        // Arrange
        Long chatId = 1L;
        List<String> tags = Arrays.asList("tag1", "tag2");
        linkStateRepository.setTags(chatId, tags);

        // Act & Assert
        assertEquals(tags, linkStateRepository.getTags(chatId));
    }

    @Test
    @DisplayName("setFilters() should set the filters for a chatId")
    void setFilters_ShouldSetFilters_ForChatId() {
        // Arrange
        Long chatId = 1L;
        List<String> filters = Arrays.asList("filter1", "filter2");

        // Act
        linkStateRepository.setFilters(chatId, filters);

        // Assert
        assertEquals(filters, linkStateRepository.getFilters(chatId));
    }

    @Test
    @DisplayName("getFilters() should return null if chatId is not found")
    void getFilters_ShouldReturnNull_IfChatIdNotFound() {
        // Arrange
        Long chatId = 1L;

        // Act & Assert
        assertNull(linkStateRepository.getFilters(chatId));
    }

    @Test
    @DisplayName("getFilters() should return the correct filters if chatId is found")
    void getFilters_ShouldReturnCorrectFilters_IfChatIdFound() {
        // Arrange
        Long chatId = 1L;
        List<String> filters = Arrays.asList("filter1", "filter2");
        linkStateRepository.setFilters(chatId, filters);

        // Act & Assert
        assertEquals(filters, linkStateRepository.getFilters(chatId));
    }

    @Test
    @DisplayName("clearState() should clear the state for a chatId")
    void clearState_ShouldClearState_ForChatId() {
        // Arrange
        Long chatId = 1L;
        String link = "https://example.com";
        List<String> tags = Arrays.asList("tag1", "tag2");
        List<String> filters = Arrays.asList("filter1", "filter2");

        linkStateRepository.setTrackingLink(chatId, link);
        linkStateRepository.setTags(chatId, tags);
        linkStateRepository.setFilters(chatId, filters);

        // Act
        linkStateRepository.clearState(chatId);

        // Assert
        assertNull(linkStateRepository.getTrackingLink(chatId));
        assertNull(linkStateRepository.getTags(chatId));
        assertNull(linkStateRepository.getFilters(chatId));
    }

    @Test
    @DisplayName("clearState() should do nothing if chatId is not found")
    void clearState_ShouldDoNothing_IfChatIdNotFound() {
        // Arrange
        Long chatId = 1L;

        // Act
        linkStateRepository.clearState(chatId);

        // Assert
        assertNull(linkStateRepository.getTrackingLink(chatId));
        assertNull(linkStateRepository.getTags(chatId));
        assertNull(linkStateRepository.getFilters(chatId));
    }
}
