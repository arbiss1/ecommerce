package ecommerce.web.app.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthenticateUserRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
