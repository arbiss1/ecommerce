package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.PostResponse;
import ecommerce.web.app.controller.model.PostDetails;
import ecommerce.web.app.controller.model.PostRequest;
import ecommerce.web.app.controller.model.SearchBuilderRequest;
import ecommerce.web.app.exceptions.BindigException;
import ecommerce.web.app.exceptions.ImageCustomException;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    public final UserService userService;
    public final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest postRequest, BindingResult result) throws UserNotFoundException, AuthenticationException, BindigException, ImageCustomException {
            return ResponseEntity.ok(postService.save(postRequest, userService.getAuthenticatedUser(), postRequest.getImageUrls(), result));
    }

    @PostMapping("/activate/{postId}")
    public ResponseEntity<String> activate(@PathVariable(value = "postId") String postId) throws PostCustomException {
            return ResponseEntity.ok(postService.changeStatus(postId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDetails>> list() throws PostCustomException {
            return ResponseEntity.ok(postService.findAll());
    }

    @PostMapping("/search")
    public ResponseEntity<List<PostDetails>> search(@RequestBody SearchBuilderRequest searchBuilderRequest) throws PostCustomException {
            return ResponseEntity.ok(postService.search(searchBuilderRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostDetails>> listByUser() throws PostCustomException, UserNotFoundException, AuthenticationException {
            return ResponseEntity.ok(postService.findByAuthenticatedUser(userService.getAuthenticatedUser().getId()));
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<PostResponse> edit(@PathVariable(name = "postId") String postId, @RequestBody PostRequest postRequest, BindingResult result) throws PostCustomException, UserNotFoundException, AuthenticationException, BindigException {
            return ResponseEntity.ok(postService.edit(postId, postRequest, userService.getAuthenticatedUser(), result));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "postId") String postId) throws PostCustomException {
            postService.deleteById(postId);
            return ResponseEntity.ok().build();
        }
}
