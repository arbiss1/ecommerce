package ecommerce.web.app.controller;


import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user,BindingResult result) {
        Optional<User> findIfExists = userService.findByUsername(user.getUsername());
            if (result.hasErrors()) {
                return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
            } else if (findIfExists.isPresent()) {
                return new ResponseEntity("Username exists", HttpStatus.CONFLICT);
            } else {
                userService.saveUser(user);
                return new ResponseEntity(user, HttpStatus.OK);
            }
    }

    @PostMapping("/post")
    public ResponseEntity<Post> post(@Valid @RequestBody Post post, BindingResult result){
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        }
        else{
            postService.savePost(post);
            return new ResponseEntity(post,HttpStatus.ACCEPTED);
        }
    }
}
