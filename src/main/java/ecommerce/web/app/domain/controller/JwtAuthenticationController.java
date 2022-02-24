package ecommerce.web.app.domain.controller;

import ecommerce.web.app.authentication.service.JwtTokenUtil;
import ecommerce.web.app.authentication.model.JwtResponse;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.dto.UserLoginRequest;
import ecommerce.web.app.domain.model.dto.UserRegisterRequest;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.exception.UserNotFoundException;
import ecommerce.web.app.exception.UsernameAlreadyExists;
import ecommerce.web.app.i18nConfig.MessageByLocaleImpl;
import ecommerce.web.app.domain.model.mapper.MapStructMapper;
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
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserDetailsService jwtInMemoryUserDetailsService;
	private final UserService userService;
	private final MessageByLocaleImpl messageByLocale;
	private final MapStructMapper mapStructMapper;

	public JwtAuthenticationController(AuthenticationManager authenticationManager,
									   JwtTokenUtil jwtTokenUtil,
									   UserDetailsService jwtInMemoryUserDetailsService,
									   UserService userService,
									   MessageByLocaleImpl messageByLocale,
									   MapStructMapper mapStructMapper){
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.jwtInMemoryUserDetailsService = jwtInMemoryUserDetailsService;
		this.userService = userService;
		this.messageByLocale = messageByLocale;
		this.mapStructMapper = mapStructMapper;
	}

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
											 BindingResult result) throws UsernameAlreadyExists {
		Optional<User> findIfExists = userService.findByUsername(userRegisterRequest.getUsername());
		if (result.hasErrors()) {
			return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
		} else if (findIfExists.isPresent()) {
			throw new UsernameAlreadyExists("error.409.userExists");
		} else {
			userService.saveUser(mapStructMapper.userPostDtoToUser(userRegisterRequest));
			return new ResponseEntity(userRegisterRequest, HttpStatus.OK);
		}
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
