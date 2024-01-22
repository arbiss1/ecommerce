package ecommerce.web.app.service;

import ecommerce.web.app.controller.model.GetUserResponse;
import ecommerce.web.app.controller.model.UserRequest;
import ecommerce.web.app.controller.model.UserResponse;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.exceptions.UsernameAlreadyExists;
import ecommerce.web.app.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.naming.AuthenticationException;
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

    PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final Locale locale = Locale.ENGLISH;

    public GetUserResponse get() throws UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> findUser = userRepository.findByUsername(userDetails.getUsername());
        if(findUser.isEmpty()){
            throw new UserNotFoundException(
                    messageByLocale.getMessage("error.404.userNotFound", null, locale)
            );
        }
        User user = findUser.get();
        return new GetUserResponse(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getCity(),
                user.getCountry(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress()
        );
    }

    public User getByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void deleteUser(String userId) throws UserNotFoundException{
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(messageByLocale.getMessage(
                "error.404.userNotFound", null, locale)
        ));
        userRepository.deleteById(findUser.getId());
    }

    public UserResponse save(UserRequest userRequest, BindingResult result) throws UsernameAlreadyExists {
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
        user.setModifiedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(user.getUsername());
        user.setModifiedBy(user.getUsername());
        return new UserResponse(userRepository.save(user).getId());
    }

    public User getAuthenticatedUser() throws UserNotFoundException, AuthenticationException {
        UserDetails userDetails;
        try{
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
        }catch (Exception e){
            throw new AuthenticationException(
                    messageByLocale.getMessage(
                            "error.401.auth", null, locale));
        }

        String username = userDetails.getUsername();
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(messageByLocale.getMessage(
                "error.404.userNotFound", null, locale)
        ));
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
}
