package backend.academy.bot.service;

import backend.academy.common.dto.LinkUpdate;

public interface UpdateService {

    /**
     * Processes a link update.
     *
     * @param update the link update to be processed
     */
    void processUpdate(LinkUpdate update);
}
