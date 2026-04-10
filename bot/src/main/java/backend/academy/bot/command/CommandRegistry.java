package backend.academy.bot.command;

import backend.academy.bot.service.TelegramService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandRegistry {
    private final TelegramService telegramService;
    private final List<CommandHandler> commandHandlers;

    @PostConstruct
    public void registerCommands() {
        telegramService.setMyCommands(commandHandlers);
    }
}
