package backend.academy.scrapper.tracking.supplier;

import backend.academy.scrapper.repository.entity.Link;
import backend.academy.scrapper.service.StackExchangeService;
import backend.academy.scrapper.tracking.SupportedServices;
import backend.academy.scrapper.tracking.TrackingEvent;
import backend.academy.scrapper.tracking.pattern.StackOverflowPattern;
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
public class StackOverflowSupplier implements TrackingSupplier {
    public static final String STACKOVERFLOW_SITE = "stackoverflow";

    private final StackExchangeService stackExchangeService;

    @Override
    public List<TrackingEvent> getEvents(Link link) {
        Matcher matcher = StackOverflowPattern.PATTERN.matcher(link.url());
        if (!matcher.matches()) {
            log.warn("Invalid URL: {}", link.url());
            return List.of();
        }

        Long questionId = Long.parseLong(matcher.group("questionId"));
        List<TrackingEvent> events = new ArrayList<>();

        stackExchangeService
                .getAnswers(questionId, STACKOVERFLOW_SITE, link.lastUpdated().toEpochSecond())
                .stream()
                .map(answer -> new TrackingEvent(
                        "[Answer](%s) posted on StackOverflow question [%d](%s)"
                                .formatted(link.url(), questionId, link.url()),
                        "[%s](%s) answered the question."
                                .formatted(
                                        answer.author().displayName(),
                                        answer.author().profileUrl()),
                        answer.createdAt()))
                .forEach(events::add);

        stackExchangeService
                .getComments(questionId, STACKOVERFLOW_SITE, link.lastUpdated().toEpochSecond())
                .stream()
                .map(comment -> new TrackingEvent(
                        "[Comment](%s) added on StackOverflow question [%d](%s)"
                                .formatted(link.url(), questionId, link.url()),
                        "[%s](%s) commented: '%s'"
                                .formatted(
                                        comment.author().displayName(),
                                        comment.author().profileUrl(),
                                        "..."),
                        comment.createdAt()))
                .forEach(events::add);

        return events.stream()
                .sorted(Comparator.comparing(TrackingEvent::timestamp))
                .toList();
    }

    @Override
    public SupportedServices getService() {
        return SupportedServices.STACKOVERFLOW_QUESTION;
    }
}
