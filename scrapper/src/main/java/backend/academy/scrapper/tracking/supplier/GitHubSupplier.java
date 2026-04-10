package backend.academy.scrapper.tracking.supplier;

import backend.academy.scrapper.client.GitHubRestClient;
import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import backend.academy.scrapper.tracking.pattern.GitHubPattern;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GitHubSupplier implements TrackingSupplier {
    private final GitHubRestClient gitHubRestClient;

    @Override
    public List<TrackingEvent> getEvents(Link link) {
        Matcher matcher = GitHubPattern.PATTERN.matcher(link.url());
        if (!matcher.matches()) {
            log.warn("Invalid URL: {}", link.url());
            return List.of();
        }

        String owner = matcher.group("owner");
        String repo = matcher.group("repo");

        List<TrackingEvent> events = new ArrayList<>();

        gitHubRestClient.getCommits(owner, repo, link.lastUpdated()).stream()
                .map(commit -> new TrackingEvent(
                        "[Commit](%s) in [%s](%s)".formatted(commit.url(), owner + "/" + repo, link.url()),
                        "[%s](%s) committed: '%s'"
                                .formatted(
                                        commit.author().login(),
                                        commit.author().url(),
                                        commit.commit().message()),
                        commit.commit().committer().createdAt()))
                .forEach(events::add);

        gitHubRestClient.getIssues(owner, repo, link.lastUpdated()).stream()
                .map(issue -> new TrackingEvent(
                        "[Issue](%s) Created in [%s](%s)".formatted(issue.url(), owner + "/" + repo, link.url()),
                        "[%s](%s) created issue: '%s'"
                                .formatted(issue.user().login(), issue.user().url(), issue.title()),
                        issue.createdAt()))
                .forEach(events::add);

        gitHubRestClient.getIssueComments(owner, repo, link.lastUpdated()).stream()
                .map(comment -> new TrackingEvent(
                        "[Issue Comment](%s) in [%s](%s)".formatted(comment.url(), owner + "/" + repo, link.url()),
                        "[%s](%s) commented on an issue: '%s'"
                                .formatted(
                                        comment.user().login(), comment.user().url(), comment.body()),
                        comment.createdAt()))
                .forEach(events::add);

        return events.stream()
                .sorted(Comparator.comparing(TrackingEvent::timestamp))
                .toList();
    }

    @Override
    public SupportedServices getService() {
        return SupportedServices.GITHUB_REPOSITORY;
    }
}
