package backend.academy.bot.command;

import backend.academy.bot.model.BotState;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.service.TrackLinkService;
import backend.academy.bot.state.StateMachine;
import backend.academy.common.dto.RemoveLinkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements CommandHandler {
    private final TelegramService telegramService;
    private final TrackLinkService trackLinkService;
    private final StateMachine stateMachine;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Удалить отслеживание ссылки.";
    }

    @Override
    public void handle(Long chatId, String[] args) {
        if (args.length > 0) {
            String link = args[0];
            trackLinkService.untrackLink(chatId, new RemoveLinkRequest(link));
        } else {
            stateMachine.setState(chatId, BotState.AWAITING_UNTRACK_LINK);
            telegramService.sendMessage(chatId, "Введите ссылку, которую хотите удалить:");
        }
    }
}
