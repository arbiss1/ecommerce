package ecommerce.web.app.service;

import ecommerce.web.app.controller.model.UserRequest;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.exceptions.UsernameAlreadyExists;
import ecommerce.web.app.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {

    public final UserRepository userRepository;
    private final MessageSource messageByLocale;

    public UserService(UserRepository userRepository, MessageSource messageByLocale){
        this.userRepository = userRepository;
        this.messageByLocale = messageByLocale;
    }

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final Locale locale = Locale.ENGLISH;

    public User findById(String userId) throws UserNotFoundException {
        Optional<User> findUser = userRepository.findById(userId);
        if(findUser.isEmpty()){
            throw new UserNotFoundException(
                    messageByLocale.getMessage("error.404.userNotFound", null, locale)
            );
        }
        return findUser.get();
    }

    public void deleteUser(String userId) throws UserNotFoundException {
        userRepository.deleteById(findById(userId).getId());
    }

    public User saveUser(UserRequest userRequest, BindingResult result) throws UsernameAlreadyExists {
        if (result.hasErrors()) {
            throw new UsernameAlreadyExists(
                    messageByLocale.getMessage(result.getAllErrors().toString(), null, locale)
            );
        }
        Optional<User> findIfUserIsIdentified = userRepository.findByUsernameOrEmailOrPhoneNumber(
                userRequest.getUsername(),
                userRequest.getEmail(),
                userRequest.getPhoneNumber()
        );

        if(findIfUserIsIdentified.isPresent()){
            throw new UsernameAlreadyExists(messageByLocale.getMessage("error.409.userExists",null,locale));
        }

        User user = mapUser(userRequest);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setLastModifiedDate(LocalDateTime.now());
        user.setCreatedDate(LocalDateTime.now());
        user.setCreatedBy(user.getUsername());
        user.setLastModifiedBy(user.getUsername());
        return userRepository.save(user);
    }

    public User mapUser(UserRequest userRequest){
        return new User(
                userRequest.getUsername(),
                userRequest.getPassword(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getCity(),
                userRequest.getCountry(),
                userRequest.getEmail(),
                userRequest.getPhoneNumber(),
                userRequest.getAddress(),
                "USER"
        );
    }

    public User getAuthenticatedUser() throws UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> response = userRepository.findByUsername(username);
        if(response.isEmpty()){
            throw new UserNotFoundException(messageByLocale.getMessage(
                    "error.404.userNotFound", null, locale)
            );
        }
        return userRepository.findByUsername(username).get();
    }

}
