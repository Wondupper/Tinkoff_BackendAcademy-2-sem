package backend.academy.bot.state;

import static backend.academy.bot.constants.RegexpConstants.WHITESPACE_PATTERN;
import static backend.academy.bot.constants.StateHandlerConstants.SKIP_INPUT;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TelegramService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwaitingTagsStateHandler implements StateHandler {
    private final LinkStateRepository linkStateRepository;
    private final TelegramService telegramService;

    @Override
    public BotState getState() {
        return BotState.AWAITING_TAGS;
    }

    @Override
    public BotState handle(Long chatId, String input) {
        log.info("Handling state {}", getState());

        if (!SKIP_INPUT.equalsIgnoreCase(input)) {
            linkStateRepository.setTags(chatId, Arrays.asList(input.split(WHITESPACE_PATTERN)));
        }
        telegramService.sendMessage(chatId, "Введите фильтры (опционально) или отправьте 'Пропустить'.");
        return BotState.AWAITING_FILTERS;
    }
}
