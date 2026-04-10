package backend.academy.bot.service;

import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.RemoveLinkRequest;

public interface TrackLinkService {

    /**
     * Tracks a link for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param request request containing details of the link to be tracked
     */
    void trackLink(Long chatId, AddLinkRequest request);

    /**
     * Untracks a link for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param request request containing details of the link to be untracked
     */
    void untrackLink(Long chatId, RemoveLinkRequest request);
}
