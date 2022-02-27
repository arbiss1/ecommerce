package ecommerce.web.app.exception.customExceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsernameAlreadyExists extends Exception {
    public UsernameAlreadyExists(String msg) {
        super(msg);
    }

    public UsernameAlreadyExists(String msg, Throwable cause) {
        super(msg, cause);
    }
}
