package ecommerce.web.app.domain.controller;

import ecommerce.web.app.authentication.service.JwtTokenUtil;
import ecommerce.web.app.authentication.model.JwtResponse;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.UserLoginRequest;
import ecommerce.web.app.domain.model.dto.UserRegisterRequest;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.exception.customExceptions.EmailAlreadyExists;
import ecommerce.web.app.exception.customExceptions.PhoneNumberAlreadyExists;
import ecommerce.web.app.exception.customExceptions.UsernameAlreadyExists;
import ecommerce.web.app.domain.model.mapper.MapStructMapper;
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
	private final MapStructMapper mapStructMapper;

	public JwtAuthenticationController(AuthenticationManager authenticationManager,
									   JwtTokenUtil jwtTokenUtil,
									   UserDetailsService jwtInMemoryUserDetailsService,
									   UserService userService,
									   MessageSource messageByLocale,
									   MapStructMapper mapStructMapper){
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
		this.userService = userService;
		this.messageByLocale = messageByLocale;
		this.mapStructMapper = mapStructMapper;
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
            userService.saveUser(mapStructMapper.userPostDtoToUser(userRegisterRequest));
        }
        return new ResponseEntity(userRegisterRequest, HttpStatus.OK);
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
