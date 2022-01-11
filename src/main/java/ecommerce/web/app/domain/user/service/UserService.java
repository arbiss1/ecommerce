package ecommerce.web.app.domain.user.service;

import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public List<User> listAll(){
        userRepository.findAll().forEach(user -> {
            System.out.println(user);
        });
        return userRepository.findAll();
    }

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

    public boolean isUsernamePresent(User user) {
        return listAll().stream().anyMatch(username -> username.getUsername().equals(user.getUsername()));
    }

//    public boolean isUserValid(User user ){
//        return listAll().stream().anyMatch(p -> p.getPassword().equals(user.getPassword())
//                && p.getUsername().equals(user.getUsername()));
//    }

}
