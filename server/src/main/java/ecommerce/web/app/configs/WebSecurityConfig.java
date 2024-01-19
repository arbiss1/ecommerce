package ecommerce.web.app.configs;

import ecommerce.web.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final AuthEntryPointJwt authEntryPointJwt;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return userService::getByUsername;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeRequests().requestMatchers("/**").permitAll();

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(new JwtFilter(userService), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.addFilterBefore(new JwtFilter((userService)), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests(authorizeRequests ->
						authorizeRequests
								.requestMatchers("/api/**").permitAll() // Secure all API endpoints
								.requestMatchers("/event/findAll").permitAll() // Exclude this specific endpoint
								.anyRequest().permitAll() // Require authentication for all other requests
				)
				.csrf().disable()
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider());
	}
}
