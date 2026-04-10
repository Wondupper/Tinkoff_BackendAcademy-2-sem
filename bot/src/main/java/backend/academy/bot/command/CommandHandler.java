package backend.academy.bot.command;

public interface CommandHandler {

    /** Get command name */
    String command();

    /** Get command description */
    String description();

    /**
     * Handle command
     *
     * @param chatId chat id
     * @param args command arguments
     */
    void handle(Long chatId, String[] args);
}
