package backend.academy.scrapper.tracking.supplier;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.client.GitHubRestClient;
import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import backend.academy.scrapper.tracking.model.GitHubCommitInfo;
import backend.academy.scrapper.tracking.model.GitHubCommitInfo.CommitInfo.CommiterInfo;
import backend.academy.scrapper.tracking.model.GitHubIssueCommentInfo;
import backend.academy.scrapper.tracking.model.GitHubIssueInfo;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GitHubSupplierTest {
    @Mock
    private GitHubRestClient gitHubRestClient;

    @InjectMocks
    private GitHubSupplier gitHubSupplier;

    private static final OffsetDateTime DATE_TIME = OffsetDateTime.now();
    private static final String OWNER = "owner";
    private static final String REPO = "repo";
    private static final Link LINK =
            new Link(1L, "https://github.com/owner/repo", SupportedServices.GITHUB_REPOSITORY, DATE_TIME.minusDays(1));

    @Test
    @DisplayName("getEvents() should return empty list when URL is invalid")
    void getEvents_ShouldReturnEmptyList_WhenUrlIsInvalid() {
        // Arrange
        Link invalidLink =
                new Link(1L, "https://example.com", SupportedServices.GITHUB_REPOSITORY, DATE_TIME.plusDays(1));

        // Act
        List<TrackingEvent> events = gitHubSupplier.getEvents(invalidLink);

        // Assert
        assertThat(events).isEmpty();
        verify(gitHubRestClient, never()).getCommits(any(), any(), any());
        verify(gitHubRestClient, never()).getIssues(any(), any(), any());
        verify(gitHubRestClient, never()).getIssueComments(any(), any(), any());
    }

    @Test
    @DisplayName("getEvents() should return commit events when valid URL")
    void getEvents_ShouldReturnCommitEvents_WhenValidUrlAndNewCommits() {
        // Arrange
        TrackingEvent expectedEvent = new TrackingEvent(
                "[Commit](url) in [owner/repo](%s)".formatted(LINK.url()),
                "[author](author-url) committed: 'fix issue'",
                DATE_TIME.minusHours(2));

        when(gitHubRestClient.getCommits(OWNER, REPO, LINK.lastUpdated()))
                .thenReturn(List.of(new GitHubCommitInfo(
                        "url",
                        new GitHubCommitInfo.CommitInfo("fix issue", new CommiterInfo(DATE_TIME.minusHours(2))),
                        new GitHubCommitInfo.AuthorInfo("author", "author-url"))));

        // Act
        List<TrackingEvent> events = gitHubSupplier.getEvents(LINK);

        // Assert
        assertThat(events).containsExactly(expectedEvent);
        verify(gitHubRestClient).getCommits(any(), any(), any());
        verify(gitHubRestClient).getIssues(any(), any(), any());
        verify(gitHubRestClient).getIssueComments(any(), any(), any());
    }

    @Test
    @DisplayName("getEvents() should return issue events when valid URL")
    void getEvents_ShouldReturnIssueEvents_WhenValidUrlAndNewIssues() {
        // Arrange
        TrackingEvent expectedEvent = new TrackingEvent(
                "[Issue](url) Created in [owner/repo](%s)".formatted(LINK.url()),
                "[user](user-url) created issue: 'Issue Title'",
                DATE_TIME.minusHours(1));

        when(gitHubRestClient.getIssues(OWNER, REPO, LINK.lastUpdated()))
                .thenReturn(List.of(new GitHubIssueInfo(
                        "url",
                        "Issue Title",
                        new GitHubIssueInfo.UserInfo("user", "user-url"),
                        DATE_TIME.minusHours(1))));

        // Act
        List<TrackingEvent> events = gitHubSupplier.getEvents(LINK);

        // Assert
        assertThat(events).containsExactly(expectedEvent);
        verify(gitHubRestClient).getCommits(any(), any(), any());
        verify(gitHubRestClient).getIssues(any(), any(), any());
        verify(gitHubRestClient).getIssueComments(any(), any(), any());
    }

    @Test
    @DisplayName("getEvents() should return issue comment events when valid URL")
    void getEvents_ShouldReturnIssueCommentEvents_WhenValidUrlAndNewComments() {
        // Arrange
        TrackingEvent expectedEvent = new TrackingEvent(
                "[Issue Comment](url) in [owner/repo](%s)".formatted(LINK.url()),
                "[user](user-url) commented on an issue: 'This is a comment'",
                DATE_TIME.minusHours(1));

        when(gitHubRestClient.getIssueComments(OWNER, REPO, LINK.lastUpdated()))
                .thenReturn(List.of(new GitHubIssueCommentInfo(
                        "url",
                        "This is a comment",
                        new GitHubIssueCommentInfo.UserInfo("user", "user-url"),
                        DATE_TIME.minusHours(1))));

        // Act
        List<TrackingEvent> events = gitHubSupplier.getEvents(LINK);

        // Assert
        assertThat(events).containsExactly(expectedEvent);
        verify(gitHubRestClient).getCommits(any(), any(), any());
        verify(gitHubRestClient).getIssues(any(), any(), any());
        verify(gitHubRestClient).getIssueComments(any(), any(), any());
    }
}
