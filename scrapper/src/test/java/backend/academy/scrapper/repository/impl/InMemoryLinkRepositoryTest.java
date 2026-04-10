package backend.academy.scrapper.repository.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import backend.academy.scrapper.repository.entity.Link;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryLinkRepositoryTest {
    private InMemoryLinkRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLinkRepository();
    }

    @Test
    @DisplayName("save() should save new link when url is unique")
    void save_ShouldSaveNewLink_WhenUrlIsUnique() {
        // Arrange
        Link link = new Link(null, "https://example.com", null, null);

        // Act
        Link savedLink = repository.save(link);

        // Assert
        assertThat(savedLink).isNotNull();
        assertThat(savedLink.id()).isNotNull();
        assertThat(savedLink.url()).isEqualTo(link.url());
    }

    @Test
    @DisplayName("save() should return existing link when url already exists")
    void save_ShouldReturnExistingLink_WhenUrlAlreadyExists() {
        // Arrange
        Link link1 = new Link(null, "https://example.com", null, null);
        Link savedLink1 = repository.save(link1);

        Link link2 = new Link(null, "https://example.com", null, null);

        // Act
        Link savedLink2 = repository.save(link2);

        // Assert
        assertThat(savedLink2).isNotNull().isEqualTo(savedLink1);
        assertThat(savedLink2.id()).isEqualTo(savedLink1.id());
    }

    @Test
    @DisplayName("findAll() should return all links")
    void findAll_ShouldReturnAllLinks() {
        // Arrange
        Link link1 = new Link(null, "https://example1.com", null, null);
        Link link2 = new Link(null, "https://example2.com", null, null);
        repository.save(link1);
        repository.save(link2);

        // Act
        List<Link> links = repository.findAll();

        // Assert
        assertThat(links).hasSize(2);
        assertThat(links).contains(link1, link2);
    }

    @Test
    @DisplayName("findAll() should return empty list")
    void findAll_ShouldReturnEmptyList() {
        // Act
        List<Link> links = repository.findAll();

        // Assert
        assertThat(links).isEmpty();
    }

    @Test
    @DisplayName("findByUrl() should return link_ when url exists")
    void findByUrl_ShouldReturnLink_WhenUrlExists() {
        // Arrange
        Link savingLink = new Link(null, "https://example.com", null, null);
        repository.save(savingLink);

        // Act
        Optional<Link> foundLink = repository.findByUrl("https://example.com");

        // Assert
        assertThat(foundLink).isPresent().hasValueSatisfying(link -> assertThat(link.url())
                .isEqualTo(savingLink.url()));
    }

    @Test
    @DisplayName("findByUrl() should return empty when url does not exist")
    void findByUrl_ShouldReturnEmpty_WhenUrlDoesNotExist() {
        // Act
        Optional<Link> foundLink = repository.findByUrl("https://notfound.com");

        // Assert
        assertThat(foundLink).isEmpty();
    }
}
