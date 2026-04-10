package backend.academy.scrapper.service;

import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;

public interface LinkService {

    /**
     * Retrieves all links associated with a specified chat.
     *
     * @param chatId the unique identifier of the chat
     * @return a response containing the list of links
     */
    ListLinksResponse getLinks(Long chatId);

    /**
     * Adds a link to a specified chat.
     *
     * @param chatId the unique identifier of the chat
     * @param request the request containing details of the link to be added
     * @return a response containing details of the added link
     */
    LinkResponse addLink(Long chatId, AddLinkRequest request);

    /**
     * Removes a link from a specified chat.
     *
     * @param chatId the unique identifier of the chat
     * @param request the request containing details of the link to be removed
     * @return a response containing details of the removed link
     */
    LinkResponse removeLink(Long chatId, RemoveLinkRequest request);
}
