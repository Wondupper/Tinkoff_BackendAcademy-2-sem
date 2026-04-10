package backend.academy.scrapper.tracking.supplier;

import static backend.academy.scrapper.tracking.supplier.StackOverflowSupplier.STACKOVERFLOW_SITE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.service.StackExchangeService;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import backend.academy.scrapper.tracking.model.StackExchangeAnswerInfo;
import backend.academy.scrapper.tracking.model.StackExchangeCommentInfo;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StackOverflowSupplierTest {
    @Mock
    private StackExchangeService stackExchangeService;

    @InjectMocks
    private StackOverflowSupplier stackOverflowSupplier;

    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now();
    private static final Long QUESTION_ID = 123456L;
    private static final Link LINK = new Link(
            1L,
            "https://stackoverflow.com/questions/123456",
            SupportedServices.STACKOVERFLOW_QUESTION,
            DATE_TIME.minusDays(1));

    @Test
    @DisplayName("getEvents() should return empty list when URL is invalid")
    void getEvents_ShouldReturnEmptyList_WhenUrlIsInvalid() {
        // Arrange
        Link invalidLink =
                new Link(1L, "https://example.com", SupportedServices.STACKOVERFLOW_QUESTION, DATE_TIME.minusDays(1));

        // Act
        List<TrackingEvent> events = stackOverflowSupplier.getEvents(invalidLink);

        // Assert
        assertThat(events).isEmpty();
        verify(stackExchangeService, never()).getAnswers(any(), any(), any());
        verify(stackExchangeService, never()).getComments(any(), any(), any());
    }

    @Test
    @DisplayName("getEvents() should return answer events when valid URL and new answers exist")
    void getEvents_ShouldReturnAnswerEvents_WhenValidUrlAndNewAnswers() {
        // Arrange
        TrackingEvent expectedEvent = new TrackingEvent(
                "[Answer](%s) posted on StackOverflow question [%d](%s)".formatted(LINK.url(), QUESTION_ID, LINK.url()),
                "[Sour Soup](profile-url) answered the question.",
                DATE_TIME.minusHours(2));

        when(stackExchangeService.getAnswers(
                        QUESTION_ID, STACKOVERFLOW_SITE, LINK.lastUpdated().toEpochSecond()))
                .thenReturn(List.of(new StackExchangeAnswerInfo(
                        DATE_TIME.minusHours(2), new StackExchangeAnswerInfo.AuthorInfo("Sour Soup", "profile-url"))));

        // Act
        List<TrackingEvent> events = stackOverflowSupplier.getEvents(LINK);

        // Assert
        assertThat(events).containsExactly(expectedEvent);
        verify(stackExchangeService).getAnswers(any(), any(), any());
        verify(stackExchangeService).getComments(any(), any(), any());
    }

    @Test
    @DisplayName("getEvents() should return comment events when valid URL and new comments exist")
    void getEvents_ShouldReturnCommentEvents_WhenValidUrlAndNewComments() {
        // Arrange
        TrackingEvent expectedEvent = new TrackingEvent(
                "[Comment](%s) added on StackOverflow question [%d](%s)".formatted(LINK.url(), QUESTION_ID, LINK.url()),
                "[Sour Soup](profile-url) commented: '...'",
                DATE_TIME.minusHours(1));

        when(stackExchangeService.getComments(
                        QUESTION_ID, STACKOVERFLOW_SITE, LINK.lastUpdated().toEpochSecond()))
                .thenReturn(List.of(new StackExchangeCommentInfo(
                        DATE_TIME.minusHours(1), new StackExchangeCommentInfo.AuthorInfo("Sour Soup", "profile-url"))));

        // Act
        List<TrackingEvent> events = stackOverflowSupplier.getEvents(LINK);

        // Assert
        assertThat(events).containsExactly(expectedEvent);
        verify(stackExchangeService).getAnswers(any(), any(), any());
        verify(stackExchangeService).getComments(any(), any(), any());
    }
}
