package backend.academy.bot.repository.impl;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.StateRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryStateRepository implements StateRepository {
    private final Map<Long, BotState> states = new HashMap<>();

    @Override
    public void setState(Long chatId, BotState state) {
        states.put(chatId, state);
    }

    @Override
    public BotState getState(Long chatId) {
        return states.getOrDefault(chatId, BotState.IDLE);
    }

    @Override
    public void clearState(Long chatId) {
        states.remove(chatId);
    }
}
