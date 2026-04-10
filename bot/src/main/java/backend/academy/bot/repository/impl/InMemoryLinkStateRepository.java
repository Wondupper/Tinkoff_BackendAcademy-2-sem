package backend.academy.bot.repository.impl;

import backend.academy.bot.repository.LinkStateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLinkStateRepository implements LinkStateRepository {
    private final Map<Long, String> linksMap = new HashMap<>();
    private final Map<Long, List<String>> tagsMap = new HashMap<>();
    private final Map<Long, List<String>> filtersMap = new HashMap<>();

    @Override
    public void setTrackingLink(Long chatId, String link) {
        linksMap.put(chatId, link);
    }

    @Override
    public String getTrackingLink(Long chatId) {
        return linksMap.get(chatId);
    }

    @Override
    public void setTags(Long chatId, List<String> tags) {
        tagsMap.put(chatId, tags);
    }

    @Override
    public List<String> getTags(Long chatId) {
        return tagsMap.get(chatId);
    }

    @Override
    public void setFilters(Long chatId, List<String> filters) {
        filtersMap.put(chatId, filters);
    }

    @Override
    public List<String> getFilters(Long chatId) {
        return filtersMap.get(chatId);
    }

    @Override
    public void clearState(Long chatId) {
        linksMap.remove(chatId);
        tagsMap.remove(chatId);
        filtersMap.remove(chatId);
    }
}
