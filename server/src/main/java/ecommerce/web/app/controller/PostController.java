package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.*;
import ecommerce.web.app.exceptions.BindingException;
import ecommerce.web.app.exceptions.ImageCustomException;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin("*")
@RequestMapping("/api/post")
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class PostController {
    public final UserService userService;
    public final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostRequest postRequest, BindingResult result
    ) throws UserNotFoundException, AuthenticationException, BindingException, ImageCustomException {
            return ResponseEntity.ok(postService.save(postRequest, userService.getAuthenticatedUser(), postRequest.getImageUrls(), result));
    }

    @PostMapping("/activate/{postId}")
    public ResponseEntity<String> activate(@PathVariable(value = "postId") String postId) throws PostCustomException {
            return ResponseEntity.ok(postService.changeStatus(postId));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PostDetails>> list(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
            return ResponseEntity.ok(postService.findAll(page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<PostDetails>> search(
            @RequestBody SearchBuilderRequest searchBuilderRequest,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
            return ResponseEntity.ok(postService.search(searchBuilderRequest, page, size));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<PostDetails>> listByUser(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) throws UserNotFoundException, AuthenticationException {
            return ResponseEntity.ok(postService.listByUser(userService.getAuthenticatedUser().getId(), page, size));
    }

    @PutMapping("/edit/{postId}")
    public ResponseEntity<EditPostResponse> edit(@PathVariable(name = "postId") String postId, @RequestBody EditPostRequest editPostRequest, BindingResult result) throws PostCustomException, UserNotFoundException, AuthenticationException, BindingException {
            return ResponseEntity.ok(postService.editPostDetails(postId, editPostRequest, userService.getAuthenticatedUser(), result));
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "postId") String postId) throws PostCustomException {
            postService.deleteById(postId);
            return ResponseEntity.ok().build();
        }
}
