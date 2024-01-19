package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.*;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.exceptions.UsernameAlreadyExists;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final UserService userService;
    private final AuthService authenticationService;

    public AuthController(UserService userService, AuthService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthUserResponse> createAuthenticationToken(@RequestBody AuthUserRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) throws UsernameAlreadyExists {
        return ResponseEntity.ok(userService.save(userRequest, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable(name = "id") String id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") String userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
