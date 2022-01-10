package ecommerce.web.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserGetDto {

    @JsonProperty("id")
    private long id ;

    @JsonProperty("username")
    private String username;

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
