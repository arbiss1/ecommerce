package ecommerce.web.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecommerce.web.app.annotations.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserPostDto {

    @JsonProperty("id")
    private long id ;

    @JsonProperty("username")
    @NotEmpty(message = "Username empty")
    @NotNull(message = "Username empty")
    private String username;

    @JsonProperty("password")
    @ValidPassword
//    @NotEmpty(message = "Password empty")
//    @NotNull(message = "Password empty")
    private String password;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("number")
    private long number;

    @JsonProperty("role")
    private String role;
}
