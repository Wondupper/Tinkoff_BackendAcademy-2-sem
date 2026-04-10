package backend.academy.bot.repository;

import backend.academy.bot.model.BotState;

public interface StateRepository {

    /**
     * Sets the state of the bot for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param state the state to be set for the bot
     */
    void setState(Long chatId, BotState state);

    /**
     * Returns the current state of the bot for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @return the current state of the bot or null if no state is set
     */
    BotState getState(Long chatId);

    /**
     * Clears the state of the bot for the specified chat.
     *
     * @param chatId unique identifier of the chat
     */
    void clearState(Long chatId);
}
