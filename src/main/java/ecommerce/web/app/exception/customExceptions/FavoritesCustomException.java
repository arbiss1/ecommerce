package ecommerce.web.app.exception.customExceptions;

public class FavoritesCustomException extends Throwable {
    public FavoritesCustomException(String msg) {
        super(msg);
    }

    public FavoritesCustomException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
