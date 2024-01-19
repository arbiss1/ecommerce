package ecommerce.web.app.controller.model;

import lombok.Data;

@Data
public class AuthUserRequest {

    private String username;

    private String password;
}
