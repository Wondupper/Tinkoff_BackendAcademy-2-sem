package backend.academy.scrapper.repository.impl;

import backend.academy.scrapper.repository.TrackedLinkRepository;
import backend.academy.scrapper.repository.entity.TrackedLink;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryTrackedLinkRepository implements TrackedLinkRepository {
    private final Map<Long, TrackedLink> storage = new HashMap<>();
    private final Map<Long, Set<Long>> chatIndex = new HashMap<>();
    private final Map<String, Set<Long>> urlIndex = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public TrackedLink save(TrackedLink trackedLink) {
        trackedLink.id(idGenerator.getAndIncrement());

        chatIndex.computeIfAbsent(trackedLink.chat().id(), k -> new HashSet<>()).add(trackedLink.id());
        urlIndex.computeIfAbsent(trackedLink.link().url(), k -> new HashSet<>()).add(trackedLink.id());

        storage.put(trackedLink.id(), trackedLink);

        return storage.get(trackedLink.id());
    }

    @Override
    public void deleteByChatIdAndUrl(Long chatId, String linkUrl) {
        Optional<TrackedLink> trackedLink = findByChatIdAndUrl(chatId, linkUrl);

        trackedLink.ifPresent(link -> {
            storage.remove(link.id());
            chatIndex.computeIfAbsent(chatId, k -> new HashSet<>()).remove(link.id());
            urlIndex.computeIfAbsent(linkUrl, k -> new HashSet<>()).remove(link.id());
        });
    }

    @Override
    public void deleteByChatId(Long chatId) {
        Set<TrackedLink> trackedLinks = findByChatId(chatId);

        trackedLinks.forEach(link -> {
            storage.remove(link.id());
            chatIndex.computeIfAbsent(chatId, k -> new HashSet<>()).remove(link.id());
            urlIndex.computeIfAbsent(link.link().url(), k -> new HashSet<>()).remove(link.id());
        });
    }

    @Override
    public Set<TrackedLink> findByChatId(Long chatId) {
        return chatIndex.getOrDefault(chatId, Set.of()).stream()
                .map(storage::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<TrackedLink> findByUrl(String url) {
        return urlIndex.getOrDefault(url, Set.of()).stream().map(storage::get).collect(Collectors.toSet());
    }

    @Override
    public Optional<TrackedLink> findByChatIdAndUrl(Long chatId, String url) {
        return findByChatId(chatId).stream()
                .filter(trackedLink -> trackedLink.link().url().equals(url))
                .findFirst();
    }
}
