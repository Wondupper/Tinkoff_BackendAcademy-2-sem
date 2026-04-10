package backend.academy.scrapper.repository;

import backend.academy.scrapper.repository.entity.TrackedLink;
import java.util.Optional;
import java.util.Set;

public interface TrackedLinkRepository {

    /**
     * Saves a tracked link entity to the repository.
     *
     * @param trackedLink the tracked link entity to be saved
     * @return the saved tracked link entity
     */
    TrackedLink save(TrackedLink trackedLink);

    /**
     * Deletes a tracked link entity by chat ID and link URL.
     *
     * @param chatId the unique identifier of the chat
     * @param linkUrl the URL of the link
     */
    void deleteByChatIdAndUrl(Long chatId, String linkUrl);

    /**
     * Deletes all tracked link entities by chat ID.
     *
     * @param chatId the unique identifier of the chat
     */
    void deleteByChatId(Long chatId);

    /**
     * Finds all tracked link entities by chat ID.
     *
     * @param chatId the unique identifier of the chat
     * @return a set of tracked link entities associated with the chat
     */
    Set<TrackedLink> findByChatId(Long chatId);

    /**
     * Finds all tracked link entities by link URL.
     *
     * @param url the URL of the link
     * @return a set of tracked link entities with the specified URL
     */
    Set<TrackedLink> findByUrl(String url);

    /**
     * Finds a tracked link entity by chat ID and link URL.
     *
     * @param chatId the unique identifier of the chat
     * @param url the URL of the link
     * @return an Optional containing the tracked link entity if found, or an empty Optional if not found
     */
    Optional<TrackedLink> findByChatIdAndUrl(Long chatId, String url);
}
