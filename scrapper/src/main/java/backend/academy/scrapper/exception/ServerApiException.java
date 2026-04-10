package backend.academy.scrapper.exception;

public class ServerApiException extends RuntimeException {

    public ServerApiException(String message) {
        super(message);
    }
}
