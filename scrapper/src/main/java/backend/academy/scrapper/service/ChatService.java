package backend.academy.scrapper.service;

public interface ChatService {

    /**
     * Registers a chat with the specified ID.
     *
     * @param id the unique identifier of the chat to be registered
     */
    void registerChat(Long id);

    /**
     * Deletes a chat with the specified ID.
     *
     * @param id the unique identifier of the chat to be deleted
     */
    void deleteChat(Long id);
}
