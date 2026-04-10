package backend.academy.bot.command;

import static backend.academy.bot.constants.TelegramMessageConstants.ERROR_MESSAGE;

import backend.academy.bot.exception.ClientApiException;
import backend.academy.bot.exception.ServerApiException;
import backend.academy.bot.service.ScrapperService;
import backend.academy.bot.service.TelegramService;
import backend.academy.common.dto.LinkResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCommand implements CommandHandler {
    private final TelegramService telegramService;
    private final ScrapperService scrapperService;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Список отслеживаемых ссылок.";
    }

    @Override
    public void handle(Long chatId, String[] args) {
        List<LinkResponse> links;

        try {
            links = scrapperService.getLinks(chatId);
        } catch (ClientApiException | ServerApiException e) {
            log.error("Error while getting links", e);
            telegramService.sendMessage(chatId, ERROR_MESSAGE);
            return;
        }

        String response = links.isEmpty()
                ? "Вы не отслеживаете ни одной ссылки."
                : "Ваши отслеживаемые ссылки:" + System.lineSeparator()
                        + links.stream().map(LinkResponse::url).collect(Collectors.joining(System.lineSeparator()));

        telegramService.sendMessage(chatId, response);
    }
}
