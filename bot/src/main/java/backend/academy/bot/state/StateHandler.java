package backend.academy.bot.state;

import backend.academy.bot.model.BotState;

public interface StateHandler {

    /**
     * Returns the current state of the bot.
     *
     * @return the current state of the bot
     */
    BotState getState();

    /**
     * Handles the state transition based on the user input for the specified chat.
     *
     * @param chatId unique identifier of the chat
     * @param input user input to be processed
     * @return the new state of the bot after handling the input
     */
    BotState handle(Long chatId, String input);
}
