package ecommerce.web.app.controller.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserRequest {
    @NotEmpty
    @NotNull
    private String username;
    @NotEmpty
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
}
