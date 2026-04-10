package backend.academy.scrapper.service.impl;

import backend.academy.common.dto.LinkUpdate;
import backend.academy.scrapper.client.BotRestClient;
import backend.academy.scrapper.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {
    private final BotRestClient botRestClient;

    @Override
    public void processUpdate(LinkUpdate update) {
        log.info("Sending update: {}", update);

        botRestClient.sendUpdate(update);

        log.info("Update sent: {}", update);
    }
}
