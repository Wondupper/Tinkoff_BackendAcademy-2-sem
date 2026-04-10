package backend.academy.scrapper.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.repository.TrackedLinkRepository;
import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.service.UpdateService;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import backend.academy.scrapper.tracking.supplier.TrackingSupplier;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LinkUpdatesServiceTest {
    @Mock
    private LinkRepository linkRepository;

    @Mock
    private TrackedLinkRepository trackedLinkRepository;

    @Mock
    private UpdateService updateService;

    @Mock
    private TrackingSupplier trackingSupplier;

    private LinkUpdatesService linkUpdatesService;

    private static final SupportedServices TEST_SERVICE = SupportedServices.GITHUB_REPOSITORY;
    private static final Link TEST_LINK = Link.builder()
            .id(1L)
            .url("https://github.com/user/repo")
            .service(TEST_SERVICE)
            .lastUpdated(OffsetDateTime.now().minusDays(1))
            .build();
    private static final TrackingEvent TEST_EVENT =
            new TrackingEvent("test event", "test description", OffsetDateTime.now());

    @BeforeEach
    void setUp() {
        when(trackingSupplier.getService()).thenReturn(TEST_SERVICE);

        linkUpdatesService =
                new LinkUpdatesService(linkRepository, updateService, trackedLinkRepository, List.of(trackingSupplier));
    }

    @Test
    @DisplayName("updateLinks() should update all links")
    void updateLinks_ShouldProcessAllLinks() {
        // Arrange
        when(linkRepository.findAll()).thenReturn(List.of(TEST_LINK, TEST_LINK));
        when(trackingSupplier.getEvents(any())).thenReturn(List.of(TEST_EVENT));

        // Act
        linkUpdatesService.updateLinks();

        // Assert
        verify(linkRepository, times(2)).save(any());
        verify(updateService, times(2)).processUpdate(any());
    }

    @Test
    @DisplayName("updateLinks() should skip processing if no events available")
    void updateLinks_WhenNoEvents_ShouldSkipProcessing() {
        // Arrange
        when(linkRepository.findAll()).thenReturn(List.of(TEST_LINK));
        when(trackingSupplier.getEvents(TEST_LINK)).thenReturn(List.of());

        // Act
        linkUpdatesService.updateLinks();

        // Assert
        verify(linkRepository, never()).save(any());
        verify(updateService, never()).processUpdate(any());
    }
}
