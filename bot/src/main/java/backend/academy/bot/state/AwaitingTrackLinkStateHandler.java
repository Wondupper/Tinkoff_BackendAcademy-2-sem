package backend.academy.bot.state;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwaitingTrackLinkStateHandler implements StateHandler {
    private final TelegramService telegramService;
    private final LinkStateRepository linkStateRepository;

    @Override
    public BotState getState() {
        return BotState.AWAITING_TRACK_LINK;
    }

    @Override
    public BotState handle(Long chatId, String input) {
        log.info("Handling state {}", getState());

        linkStateRepository.setTrackingLink(chatId, input);

        telegramService.sendMessage(chatId, "Введите теги: (опционально) или отправьте 'Пропустить'.");

        return BotState.AWAITING_TAGS;
    }
}
