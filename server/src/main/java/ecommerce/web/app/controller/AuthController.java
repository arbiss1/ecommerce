package ecommerce.web.app.controller;

import ecommerce.web.app.configs.JwtUtils;
import ecommerce.web.app.controller.model.*;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.exceptions.UsernameAlreadyExists;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final UserService userService;
    private final AuthService authenticationService;
    private final JwtUtils jwtUtils;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthUserResponse> createAuthenticationToken(@Valid @RequestBody AuthUserRequest authenticationRequest, BindingResult result) throws UsernameAlreadyExists {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest, result));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) throws UsernameAlreadyExists {
        return ResponseEntity.ok(userService.save(userRequest, result));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) throws AuthenticationException {
        String jwtToken = extractTokenFromHeader(request);
        jwtUtils.invalidateToken(jwtToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<GetUserResponse> get() throws UserNotFoundException {
        return ResponseEntity.ok(userService.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") String id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
