package backend.academy.scrapper.repository.impl;

import backend.academy.scrapper.repository.ChatRepository;
import backend.academy.scrapper.repository.entity.Chat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class InMemoryChatRepository implements ChatRepository {
    private final Map<Long, Chat> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Chat save(Chat chat) {
        if (Objects.isNull(chat.id())) {
            chat.id(idGenerator.getAndIncrement());
        }

        storage.put(chat.id(), chat);

        return storage.get(chat.id());
    }

    @Override
    public Optional<Chat> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
