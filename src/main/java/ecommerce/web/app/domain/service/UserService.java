package ecommerce.web.app.domain.service;

import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public final UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> listAll(){
        userRepository.findAll().forEach(user -> {
            System.out.println(user);
        });
        return userRepository.findAll();
    }

    public User saveUser(User user){
        user.setRole("USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setLastModifiedDate(LocalDateTime.now());
        user.setCreatedDate(LocalDateTime.now());
        user.setCreatedBy(user.getUsername());
        user.setLastModifiedBy(user.getUsername());
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

    public boolean isUsernamePresent(User user) {
        return listAll().stream().anyMatch(username -> username.getUsername().equals(user.getUsername()));
    }

//    public boolean isUserValid(User user ){
//        return listAll().stream().anyMatch(p -> p.getPassword().equals(user.getPassword())
//                && p.getUsername().equals(user.getUsername()));
//    }

}
