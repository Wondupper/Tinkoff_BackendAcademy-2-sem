package backend.academy.bot.service;

import backend.academy.bot.command.CommandHandler;
import java.util.List;

public interface TelegramService {

    /**
     * Sends a message to the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param message the message to be sent
     */
    void sendMessage(Long chatId, String message);

    /**
     * Sets the list of commands for the bot.
     *
     * @param commands list of command handlers to be set for the bot
     */
    void setMyCommands(List<CommandHandler> commands);
}
