package backend.academy.bot.command;

import backend.academy.bot.model.BotState;
import backend.academy.bot.repository.LinkStateRepository;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.state.StateMachine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackCommand implements CommandHandler {
    private final TelegramService telegramService;
    private final StateMachine stateMachine;
    private final LinkStateRepository linkStateRepository;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Добавить новую ссылку для отслеживания.";
    }

    @Override
    public void handle(Long chatId, String[] args) {
        if (args.length > 0) {
            String link = args[0];
            linkStateRepository.setTrackingLink(chatId, link);
            stateMachine.setState(chatId, BotState.AWAITING_TAGS);
            telegramService.sendMessage(chatId, "Введите теги: (опционально) или отправьте 'Пропустить'.");
        } else {
            telegramService.sendMessage(chatId, "Введите ссылку для отслеживания:");
            stateMachine.setState(chatId, BotState.AWAITING_TRACK_LINK);
        }
    }
}
