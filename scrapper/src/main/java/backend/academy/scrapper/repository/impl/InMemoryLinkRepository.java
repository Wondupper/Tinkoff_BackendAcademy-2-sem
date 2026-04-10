package backend.academy.scrapper.repository.impl;

import backend.academy.scrapper.repository.LinkRepository;
import backend.academy.scrapper.repository.entity.Link;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryLinkRepository implements LinkRepository {
    private final Map<Long, Link> storage = new HashMap<>();
    private final Map<String, Long> urlIndex = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Link save(Link link) {
        Long existingId = urlIndex.get(link.url());
        if (Objects.nonNull(existingId)) {
            return storage.put(existingId, link);
        }

        if (Objects.isNull(link.id())) {
            link.id(idGenerator.getAndIncrement());
        }

        urlIndex.put(link.url(), link.id());
        storage.put(link.id(), link);

        return storage.get(link.id());
    }

    @Override
    public List<Link> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        Long id = urlIndex.get(url);
        if (Objects.isNull(id)) {
            return Optional.empty();
        }
        return Optional.of(storage.get(id));
    }
}
