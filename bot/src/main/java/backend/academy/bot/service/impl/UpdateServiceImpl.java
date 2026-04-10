package backend.academy.bot.service.impl;

import backend.academy.bot.service.TelegramService;
import backend.academy.bot.service.UpdateService;
import backend.academy.common.dto.LinkUpdate;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateServiceImpl implements UpdateService {
    public static final String MESSAGE_TEMPLATE = "🔔 Обновление по ссылке:%n%s%n%nОписание: %n%s";

    private final TelegramService telegramService;

    @Override
    public void processUpdate(LinkUpdate update) {
        log.info("Processing update {}", update);

        if (Objects.isNull(update.tgChatIds()) || update.tgChatIds().isEmpty()) {
            log.warn("No chat IDs provided in update: {}", update);
            throw new IllegalArgumentException("Chat IDs list cannot be empty");
        }

        String message = String.format(MESSAGE_TEMPLATE, update.url(), update.description());

        update.tgChatIds().forEach(chatId -> telegramService.sendMessage(chatId, message));
    }
}
