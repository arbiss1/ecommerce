package ecommerce.web.app.exceptions;

public class PhoneNumberAlreadyExists extends Throwable {
    public PhoneNumberAlreadyExists(String msg) {
        super(msg);
    }

    public PhoneNumberAlreadyExists(String msg, Throwable cause) {
        super(msg, cause);
    }
}
