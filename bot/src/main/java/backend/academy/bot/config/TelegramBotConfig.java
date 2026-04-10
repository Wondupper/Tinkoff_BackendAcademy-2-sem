package backend.academy.bot.config;

import backend.academy.bot.config.properties.TelegramBotProperties;
import com.pengrad.telegrambot.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(TelegramBotProperties.class)
public class TelegramBotConfig {

    @Bean
    public TelegramBot telegramBot(TelegramBotProperties telegramBotProperties) {
        return new TelegramBot(telegramBotProperties.telegramToken());
    }
}
