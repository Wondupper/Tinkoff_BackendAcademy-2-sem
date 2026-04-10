package backend.academy.bot.dispatcher;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotUpdateListener {
    private final TelegramBot telegramBot;
    private final CommandDispatcher commandDispatcher;

    @PostConstruct
    public void startListener() {
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                commandDispatcher.handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
