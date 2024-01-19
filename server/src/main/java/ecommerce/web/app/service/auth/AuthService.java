package ecommerce.web.app.service.auth;

import ecommerce.web.app.configs.JwtUtils;
import ecommerce.web.app.controller.model.AuthUserRequest;
import ecommerce.web.app.controller.model.AuthUserResponse;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthUserResponse authenticate(AuthUserRequest request) {
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getUsername(),
                           request.getPassword()
                   )
           );
       }catch (Exception e){
          throw new UsernameNotFoundException("Username cannot authenticate");
       }
        User user = userService.getByUsername(request.getUsername());
        String jwtToken = jwtUtils.generateToken(user);
        return AuthUserResponse.builder()
                .accessToken(jwtToken)
                .username(user.getUsername())
                .build();
    }
}