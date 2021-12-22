package ecommerce.web.app.controller;


import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.Categories;
import ecommerce.web.app.model.User;
import ecommerce.web.app.service.CategoryService;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.Optional;


@RestController
public class MainController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    public Optional<User> getAuthenticatedUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> userAuth = userService.findByUsername(username);

        return userAuth;
    }

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

//        Optional<Categories> findByCategory = categoryService.findByCategory(post.getPostCategories());

        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        }
//        else if(!findByCategory.isPresent()) {
//            return new ResponseEntity("Category is not present",HttpStatus.CONFLICT);
//        }
        else{
            postService.savePost(post,getAuthenticatedUser(),post.getPostImageUrl(), new File(post.getPostImageUrl()));
            return new ResponseEntity(post,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/list-all-posts")
    public ResponseEntity<Post> listAllPosts(){
        List<Post> listOfPosts = postService.findAll();
        final String[] imageUrl = {null};
        listOfPosts.forEach(post -> {
            imageUrl[0] = post.getPostImageUrl();
        });
        System.out.println(postService.getImageUploaded(imageUrl[0]));
        if(listOfPosts.isEmpty()){
            return new ResponseEntity("No posts available",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(listOfPosts,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/list-posts-by-user-auth")
    public ResponseEntity<Post> listPostsByAuthenticatedUser(){
        List<Post> listByAuthenticatedUser = postService.findByUserId(getAuthenticatedUser().get().getId());
        if(listByAuthenticatedUser.isEmpty()){
            return new ResponseEntity("No post for user" + getAuthenticatedUser().get().getUsername(),HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(listByAuthenticatedUser,HttpStatus.ACCEPTED);
        }
    }
}
