package backend.academy.scrapper.repository;

import backend.academy.scrapper.repository.entity.Chat;
import java.util.Optional;

public interface ChatRepository {

    /**
     * Saves a chat entity to the repository.
     *
     * @param chat the chat entity to be saved
     * @return the saved chat entity
     */
    Chat save(Chat chat);

    /**
     * Finds a chat entity by its unique identifier.
     *
     * @param id the unique identifier of the chat
     * @return an Optional containing the chat entity if found, or an empty Optional if not found
     */
    Optional<Chat> findById(Long id);

    /**
     * Deletes a chat entity by its unique identifier.
     *
     * @param id the unique identifier of the chat to be deleted
     */
    void deleteById(Long id);
}
