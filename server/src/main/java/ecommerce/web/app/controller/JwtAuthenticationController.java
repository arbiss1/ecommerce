package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.AuthenticateUserRequest;
import ecommerce.web.app.controller.model.UserRequest;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.exceptions.UsernameAlreadyExists;
import ecommerce.web.app.service.AuthenticationService;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.controller.model.JwtResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public JwtAuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody AuthenticateUserRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.createAuthenticationToken(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) throws UsernameAlreadyExists {
        return ResponseEntity.ok(userService.saveUser(userRequest, result).getId());
    }

    @DeleteMapping("/delete/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") String userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
