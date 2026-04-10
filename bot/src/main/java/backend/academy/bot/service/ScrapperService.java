package backend.academy.bot.service;

import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import java.util.List;

public interface ScrapperService {

    /**
     * Registers a chat with the specified chat ID.
     *
     * @param chatId unique identifier of the chat
     */
    void registerChat(Long chatId);

    /**
     * Deletes a chat with the specified chat ID.
     *
     * @param chatId unique identifier of the chat
     */
    void deleteChat(Long chatId);

    /**
     * Retrieves a list of links for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @return list of links associated with the chat
     */
    List<LinkResponse> getLinks(Long chatId);

    /**
     * Adds a link to the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param addLinkRequest request containing details of the link to be added
     * @return response containing details of the added link
     */
    LinkResponse addLink(Long chatId, AddLinkRequest addLinkRequest);

    /**
     * Removes a link from the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param removeLinkRequest request containing details of the link to be removed
     * @return response containing details of the removed link
     */
    LinkResponse removeLink(Long chatId, RemoveLinkRequest removeLinkRequest);
}
