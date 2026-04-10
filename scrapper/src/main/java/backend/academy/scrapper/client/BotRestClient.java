package backend.academy.scrapper.client;

import backend.academy.common.dto.LinkUpdate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface BotRestClient {

    /**
     * Sends a link update to the bot.
     *
     * @param update the link update to be sent
     */
    @PostExchange("/updates")
    void sendUpdate(@RequestBody LinkUpdate update);
}
