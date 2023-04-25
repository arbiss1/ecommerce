package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.UserLoginRequest;
import ecommerce.web.app.controller.model.UserRegisterRequest;
import ecommerce.web.app.exceptions.EmailAlreadyExists;
import ecommerce.web.app.exceptions.PhoneNumberAlreadyExists;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.exceptions.UsernameAlreadyExists;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.service.auth.JwtTokenUtil;
import ecommerce.web.app.controller.model.JwtResponse;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserDetailsService jwtInMemoryUserDetailsService;
	private final UserService userService;
	private final MessageSource messageByLocale;
	private final UserRepository userRepository;

	public JwtAuthenticationController(AuthenticationManager authenticationManager,
									   JwtTokenUtil jwtTokenUtil,
									   UserDetailsService jwtInMemoryUserDetailsService,
									   UserService userService,
									   MessageSource messageByLocale,
									   UserRepository userRepository){
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
		this.userService = userService;
		this.messageByLocale = messageByLocale;
		this.userRepository = userRepository;
	}

	private final Locale locale = Locale.ENGLISH;

	@PostMapping(value = "/authenticate")
	public ResponseEntity createAuthenticationToken(@RequestBody UserLoginRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest,
											 BindingResult result) throws UsernameAlreadyExists,
            PhoneNumberAlreadyExists, EmailAlreadyExists {
		Optional<User> findIfExistsUsername = userService.findByUsername(userRegisterRequest.getUsername());
		Optional<User> findIfExistsPhoneNumber = userService.findByPhoneNumber(userRegisterRequest.getPhoneNumber());
		Optional<User> findIfExistsEmail = userService.findByEmail(userRegisterRequest.getEmail());
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
		}
        if(findIfExistsUsername.isPresent()){
            throw new UsernameAlreadyExists(messageByLocale.getMessage("error.409.userExists",null,locale));
        }
        if(findIfExistsEmail.isPresent()){
            throw new EmailAlreadyExists(messageByLocale.getMessage("error.409.emailExists",null,locale));
        }
        if(findIfExistsPhoneNumber.isPresent()){
            throw new PhoneNumberAlreadyExists(messageByLocale.getMessage("error.409.phoneNumberExists",null,locale));
        }
        else {
            userService.saveUser(userRegisterRequest);
        }
        return new ResponseEntity(userRegisterRequest, HttpStatus.OK);
    }

	@DeleteMapping("/delete/user/{userId}")
	public ResponseEntity deleteUser(@PathVariable(name = "userId") long userId)
			throws UserNotFoundException {
		Optional<User> findUser = userRepository.findById(userId);
		if(!findUser.isPresent()){
			throw new UserNotFoundException(
					messageByLocale.getMessage("error.404.userNotFound", null, locale)
			);
		}else {
			userRepository.deleteById(userId);
		}
		return new ResponseEntity("User with id " + userId + " was deleted successfully",HttpStatus.ACCEPTED);
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
