package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.PostRequest;
import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@CrossOrigin
public class PostController {

    public final UserService userService;
    public final PostService postService;
    public final MessageSource messageByLocale;

    public PostController(UserService userService,
                          PostService postService,
                          MessageSource messageByLocale) {
        this.userService = userService;
        this.postService = postService;
        this.messageByLocale = messageByLocale;
    }

    private final Locale locale = Locale.ENGLISH;

    @PostMapping("/post/add")
    public ResponseEntity<Post> post(@Valid @RequestBody PostRequest postRequest, BindingResult result)
            throws InterruptedException, UserNotFoundException {
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        } else {
            List<ImageUpload> postsImageUrls = postRequest.getImageUrls();
            return new ResponseEntity(postService.savePost(postRequest
                    , userService.getAuthenticatedUser(), postsImageUrls)
                    , HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/post/status/change/{postId}")
    public ResponseEntity<Post> changePostStatusToActive(@PathVariable(value = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPostById = postService.findByPostId(postId);
        if (!findPostById.isPresent()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        } else {
            Post getFoundPost = findPostById.get();
            return new ResponseEntity(postService.changeStatusToActive(
                    getFoundPost, userService.getAuthenticatedUser()), HttpStatus.OK);
        }
    }

    @GetMapping("/post/all")
    public ResponseEntity<Post> listAllPosts() throws PostCustomException {
        List<Post> listOfPosts = postService.findAll();
        if (listOfPosts.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        } else {
            return new ResponseEntity(listOfPosts, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/post/search")
    public ResponseEntity<Post> searchPosts(@RequestParam String keyword)
            throws PostCustomException {
        List<Post> seachForPosts = postService.searchPosts(keyword);
        if (keyword.equals("") || keyword.equals(" ") ||
                keyword.equals(null)) {
            return new ResponseEntity(postService.findAll(), HttpStatus.ACCEPTED);
        } else if (seachForPosts.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        } else {
            return new ResponseEntity(seachForPosts, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/user/post/all")
    public ResponseEntity<Post> listPostsByAuthenticatedUser() throws PostCustomException {
        List<Post> listByAuthenticatedUser = postService.findByUserId(
                userService.getAuthenticatedUser().get().getId());
        if (listByAuthenticatedUser.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        } else {
            return new ResponseEntity(listByAuthenticatedUser, HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/post/edit/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable(name = "postId") long postId,
                                         @RequestBody PostRequest postRequest, BindingResult result)
            throws PostCustomException, UserNotFoundException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if (result.hasErrors()) {
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        } else if (findPost.isEmpty()) {
            throw new PostCustomException(messageByLocale.getMessage("error.404.postNotFound", null, locale));
        } else {
            List<ImageUpload> postsImageUrls = postRequest.getImageUrls();
            return new ResponseEntity<Post>(postService.editPost(postId, postRequest,
                    userService.getAuthenticatedUser(), postsImageUrls)
                    , HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity deletePost(@PathVariable(name = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if (findPost.isEmpty()) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound", null, locale));
        } else {
            postService.deleteById(postId);
            return new ResponseEntity("Deleted successfully", HttpStatus.ACCEPTED);
        }
    }
}
