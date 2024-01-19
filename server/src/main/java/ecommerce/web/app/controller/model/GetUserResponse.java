package ecommerce.web.app.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
    private String address;
}
