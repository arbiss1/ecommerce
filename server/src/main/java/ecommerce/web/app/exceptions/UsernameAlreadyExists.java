package ecommerce.web.app.exceptions;

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
