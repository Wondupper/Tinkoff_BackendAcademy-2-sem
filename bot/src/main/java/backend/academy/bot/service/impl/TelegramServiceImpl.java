package backend.academy.bot.service.impl;

import backend.academy.bot.command.CommandHandler;
import backend.academy.bot.service.TelegramService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final TelegramBot telegramBot;

    @Override
    public void sendMessage(Long chatId, String message) {
        log.info("Send message to telegram chatId: {} message: {}", chatId, message);
        telegramBot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
    }

    @Override
    public void setMyCommands(List<CommandHandler> commands) {
        log.info("Set my commands: {}", commands);

        BotCommand[] botCommands = commands.stream()
                .map(cmd -> new BotCommand(cmd.command(), cmd.description()))
                .toArray(BotCommand[]::new);

        telegramBot.execute(new SetMyCommands(botCommands));
    }
}
