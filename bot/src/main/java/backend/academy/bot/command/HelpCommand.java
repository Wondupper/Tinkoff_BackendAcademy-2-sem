package backend.academy.bot.command;

import backend.academy.bot.service.TelegramService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements CommandHandler {
    private final TelegramService telegramService;
    private final List<CommandHandler> commandsHandlers;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Показать список доступных команд.";
    }

    @Override
    public void handle(Long chatId, String[] args) {
        String helpText = commandsHandlers.stream()
                .map(cmd -> cmd.command() + " - " + cmd.description())
                .collect(Collectors.joining("\n"));

        telegramService.sendMessage(chatId, helpText);
    }
}
