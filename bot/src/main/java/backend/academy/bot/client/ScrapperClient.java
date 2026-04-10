package backend.academy.bot.client;

import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.LinkResponse;
import backend.academy.common.dto.ListLinksResponse;
import backend.academy.common.dto.RemoveLinkRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface ScrapperClient {
    String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    /**
     * Register chat
     *
     * @param id chat id
     */
    @PostExchange("/tg-chat/{id}")
    void registerChat(@PathVariable Long id);

    /**
     * Delete chat
     *
     * @param id chat id
     */
    @DeleteExchange("/tg-chat/{id}")
    void deleteChat(@PathVariable Long id);

    /**
     * Get links
     *
     * @param chatId chat id
     * @return list of links
     */
    @GetExchange(value = "/links")
    ListLinksResponse getLinks(@RequestHeader(TG_CHAT_ID_HEADER) Long chatId);

    /**
     * Add link
     *
     * @param chatId chat id
     * @param request add link request
     * @return link response
     */
    @PostExchange(value = "/links")
    LinkResponse addLink(@RequestHeader(TG_CHAT_ID_HEADER) Long chatId, @RequestBody AddLinkRequest request);

    /**
     * Remove link
     *
     * @param chatId chat id
     * @param request remove link request
     * @return link response
     */
    @DeleteExchange(value = "/links")
    LinkResponse removeLink(@RequestHeader(TG_CHAT_ID_HEADER) Long chatId, @RequestBody RemoveLinkRequest request);
}
