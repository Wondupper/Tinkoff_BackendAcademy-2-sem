package backend.academy.bot.controller;

import backend.academy.bot.service.UpdateService;
import backend.academy.common.api.BotApi;
import backend.academy.common.dto.LinkUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController implements BotApi {
    private final UpdateService updateService;

    @Override
    public void sendUpdate(LinkUpdate update) {
        updateService.processUpdate(update);
    }
}
