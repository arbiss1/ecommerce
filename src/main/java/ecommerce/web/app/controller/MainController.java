package ecommerce.web.app.controller;


import ecommerce.web.app.model.ImageUpload;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.Categories;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.dto.UserPostDto;
import ecommerce.web.app.model.mapper.MapStructMapper;
import ecommerce.web.app.service.CategoryService;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    MapStructMapper mapStructMapper;

    public Optional<User> getAuthenticatedUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();

        Optional<User> userAuth = userService.findByUsername(username);

        return userAuth;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserPostDto userPostDto, BindingResult result) {
        Optional<User> findIfExists = userService.findByUsername(userPostDto.getUsername());
            if (result.hasErrors()) {
                return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
            } else if (findIfExists.isPresent()) {
                return new ResponseEntity("Username exists", HttpStatus.CONFLICT);
            } else {
                userService.saveUser(mapStructMapper.userPostDtoToUser(userPostDto));
                return new ResponseEntity(userPostDto, HttpStatus.OK);
            }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE , value = "/post")
    public ResponseEntity<Post> post(@Valid @RequestBody Post post, BindingResult result){

//        Optional<Categories> findByCategory = categoryService.findByCategory(post.getPostCategories());

        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        }
//        else if(!findByCategory.isPresent()) {
//            return new ResponseEntity("Category is not present",HttpStatus.CONFLICT);
//        }
        else{
            List<ImageUpload> postsImageUrls = post.getPostImageUrl();
            postService.savePost(post,getAuthenticatedUser(),postsImageUrls);
            return new ResponseEntity(post,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/list-all-posts")
    public ResponseEntity<Post> listAllPosts(){
        List<Post> listOfPosts = postService.findAll();
        final String[] imageUrl = {null};
        listOfPosts.forEach(post -> {
//            imageUrl[0] = post.getPostImageUrl();
        });
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
