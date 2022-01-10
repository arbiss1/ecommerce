package ecommerce.web.app.domain.user.model;


import ecommerce.web.app.annotations.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id ;

    @NotEmpty(message = "Username empty")
    @NotNull(message = "Username empty")
    private String username;

    @ValidPassword
    @NotEmpty(message = "Password empty")
    @NotNull(message = "Password empty")
    private String password;

    private String firstName;

    private String lastName;

    private String address;

    private long number;

    private String role;
}
