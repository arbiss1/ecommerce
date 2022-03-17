package ecommerce.web.app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    private static final long serialVersionUID = 5926468583005150707L;

    @NotEmpty(message = "Username empty")
    @NotNull(message = "Username empty")
    private String username;
//    @ValidPassword
    @NotEmpty(message = "Password empty")
    @NotNull(message = "Password empty")
    private String password;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
}
