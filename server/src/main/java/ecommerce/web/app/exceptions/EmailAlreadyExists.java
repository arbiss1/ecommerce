package ecommerce.web.app.exceptions;

public class EmailAlreadyExists extends Throwable {
    public EmailAlreadyExists(String msg) {
        super(msg);
    }

    public EmailAlreadyExists(String msg, Throwable cause) {
        super(msg, cause);
    }
}
