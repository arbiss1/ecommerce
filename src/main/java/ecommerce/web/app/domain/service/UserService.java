package ecommerce.web.app.domain.service;

import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User saveUser(User user){
        user.setRole("USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getAuthenticatedUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> userAuth = userRepository.findByUsername(username);

        return userAuth;
    }

    public Optional<User> findByUsername(String username){
       return userRepository.findByUsername(username);
    }

}
