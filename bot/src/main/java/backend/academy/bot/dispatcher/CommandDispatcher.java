package backend.academy.bot.dispatcher;

import static backend.academy.bot.constants.RegexpConstants.WHITESPACE_PATTERN;

import backend.academy.bot.command.CommandHandler;
import backend.academy.bot.model.BotState;
import backend.academy.bot.service.TelegramService;
import backend.academy.bot.state.StateMachine;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CommandDispatcher {
    private final StateMachine stateMachine;
    private final TelegramService telegramService;
    private final Map<String, CommandHandler> commandHandlers;

    public CommandDispatcher(
            StateMachine stateMachine, TelegramService telegramService, List<CommandHandler> commandHandlers) {
        this.stateMachine = stateMachine;
        this.telegramService = telegramService;
        this.commandHandlers =
                commandHandlers.stream().collect(Collectors.toMap(CommandHandler::command, Function.identity()));
    }

    public void handleUpdate(Update update) {
        if (Objects.isNull(update.message()) || Objects.isNull(update.message().text())) {
            log.info("Message is empty");
            return;
        }

        Message message = update.message();
        Long chatId = message.chat().id();
        String text = message.text().trim();

        String[] parts = text.split(WHITESPACE_PATTERN, 2);
        String commandKey = parts[0].toLowerCase();
        String[] args = parts.length > 1 ? parts[1].split(WHITESPACE_PATTERN) : new String[0];

        if (commandHandlers.containsKey(commandKey)) {
            log.info("Command handler for command '{}' found", commandKey);
            commandHandlers.get(commandKey).handle(chatId, args);
        } else if (!BotState.IDLE.equals(stateMachine.getState(chatId))) {
            stateMachine.handleStatefulInput(chatId, text);
        } else {
            log.info("Command '{}' not found", commandKey);
            telegramService.sendMessage(chatId, "Команда не найдена");
        }
    }
}
