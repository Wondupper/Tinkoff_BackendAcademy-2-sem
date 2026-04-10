package backend.academy.bot.service.impl;

import static backend.academy.bot.constants.TelegramMessageConstants.ERROR_MESSAGE;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.bot.service.ScrapperService;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.common.dto.AddLinkRequest;
import backend.academy.common.dto.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackLinkServiceImpl implements TrackLinkService {
    private final ScrapperService scrapperService;
    private final TelegramService telegramService;

    @Override
    public void trackLink(Long chatId, AddLinkRequest request) {
        try {
            scrapperService.addLink(chatId, request);
            telegramService.sendMessage(chatId, "Теперь эта ссылка отслеживается :)");
        } catch (ServerApiException | ClientApiException e) {
            log.error("Error while adding link", e);
            telegramService.sendMessage(chatId, ERROR_MESSAGE);
        }
    }

    @Override
    public void untrackLink(Long chatId, RemoveLinkRequest request) {
        try {
            scrapperService.removeLink(chatId, request);
            telegramService.sendMessage(chatId, "Ссылка удалена.");
        } catch (ServerApiException | ClientApiException e) {
            log.error("Error while removing link", e);
            telegramService.sendMessage(chatId, ERROR_MESSAGE);
        }
    }
}
