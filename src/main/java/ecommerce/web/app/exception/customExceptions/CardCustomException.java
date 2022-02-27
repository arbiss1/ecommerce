package ecommerce.web.app.exception.customExceptions;

public class CardCustomException extends Throwable {
    public CardCustomException(String msg) {
        super(msg);
    }

    public CardCustomException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
