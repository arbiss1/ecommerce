package ecommerce.web.app.controller.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AuthUserRequest {
    @NotEmpty(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Passoword cannot be null")
    private String password;
}
