package ecommerce.web.app.exceptions;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
