package ecommerce.web.app.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Serial
    private static final long serialVersionUID = 5926468583005150707L;

    @NotEmpty
    @NotNull
    private String username;
    @NotEmpty
    @NotNull
    private String password;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + "ADMIN"), new SimpleGrantedAuthority("ROLE_" + "USER")));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
