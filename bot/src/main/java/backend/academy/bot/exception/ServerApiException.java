package backend.academy.bot.exception;

public class ServerApiException extends RuntimeException {

    public ServerApiException(String message) {
        super(message);
    }
}
