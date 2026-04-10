package backend.academy.bot.command;

import static backend.academy.bot.constants.TelegramMessageConstants.ERROR_MESSAGE;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.bot.service.ScrapperService;
import backend.academy.bot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements CommandHandler {
    private final TelegramService telegramService;
    private final ScrapperService scrapperService;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Зарегистрировать чат в системе.";
    }

    @Override
    public void handle(Long chatId, String[] args) {
        try {
            scrapperService.registerChat(chatId);
        } catch (ClientApiException | ServerApiException e) {
            log.error("Error while registering chat", e);
            telegramService.sendMessage(chatId, ERROR_MESSAGE);
        }
        telegramService.sendMessage(chatId, "Вы зарегистрированы!");
    }
}
