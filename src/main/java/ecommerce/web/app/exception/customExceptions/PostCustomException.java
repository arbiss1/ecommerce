package ecommerce.web.app.exception.customExceptions;

public class PostCustomException extends Throwable {
    public PostCustomException(String msg) {
        super(msg);
    }

    public PostCustomException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
