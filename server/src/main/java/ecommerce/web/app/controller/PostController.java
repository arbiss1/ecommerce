package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.PostResponse;
import ecommerce.web.app.controller.model.PostDetails;
import ecommerce.web.app.controller.model.PostRequest;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@CrossOrigin
public class PostController {

    public final UserService userService;
    public final PostService postService;
    public final MessageSource messageByLocale;

    public PostController(UserService userService, PostService postService, MessageSource messageByLocale) {
        this.userService = userService;
        this.postService = postService;
        this.messageByLocale = messageByLocale;
    }

    @PostMapping("/create/post")
    public ResponseEntity<PostResponse> createNewPost(@Valid @RequestBody PostRequest postRequest, BindingResult result) throws UserNotFoundException {
            return ResponseEntity.ok(postService.savePost(postRequest, userService.getAuthenticatedUser(), postRequest.getImageUrls(), result));
    }

    @PostMapping("/post/activate/{postId}")
    public ResponseEntity<String> activatePost(@PathVariable(value = "postId") String postId) throws PostCustomException {
            return ResponseEntity.ok(postService.changeStatusToActive(postId));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDetails>> listAllPosts() throws PostCustomException {
            return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDetails>> searchPosts(@RequestParam @NotEmpty @NotBlank String keyword) throws PostCustomException {
            return ResponseEntity.ok(postService.searchPosts(keyword));
    }

    @GetMapping("/user/post")
    public ResponseEntity<List<PostDetails>> listPostsByAuthenticatedUser() throws PostCustomException, UserNotFoundException {
            return ResponseEntity.ok(postService.findPostsByUserId(userService.getAuthenticatedUser().getId()));
    }

    @PutMapping("/edit/post/{postId}")
    public ResponseEntity<PostResponse> editPost(@PathVariable(name = "postId") String postId, @RequestBody PostRequest postRequest, BindingResult result) throws PostCustomException, UserNotFoundException {
            return ResponseEntity.ok(postService.editPost(postId, postRequest, userService.getAuthenticatedUser(), postRequest.getImageUrls(), result));
    }

    @DeleteMapping("/delete/post/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "postId") String postId) {
            postService.deleteById(postId);
            return ResponseEntity.ok().build();
        }
}
