package backend.academy.bot.repository;

import java.util.List;

public interface LinkStateRepository {

    /**
     * Sets the tracked link for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param link tracked link
     */
    void setTrackingLink(Long chatId, String link);

    /**
     * Returns the tracked link for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @return tracked link or null if no link is set
     */
    String getTrackingLink(Long chatId);

    /**
     * Sets the list of tags for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param tags list of tags
     */
    void setTags(Long chatId, List<String> tags);

    /**
     * Returns the list of tags for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @return list of tags or an empty list if no tags are set
     */
    List<String> getTags(Long chatId);

    /**
     * Sets the list of filters for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param filters list of filters
     */
    void setFilters(Long chatId, List<String> filters);

    /**
     * Returns the list of filters for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @return list of filters or an empty list if no filters are set
     */
    List<String> getFilters(Long chatId);

    /**
     * Clears all state data for the specified chat, including the tracked link, tags, and filters.
     *
     * @param chatId unique identifier of the chat
     */
    void clearState(Long chatId);
}
