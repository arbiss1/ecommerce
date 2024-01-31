package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.*;
import ecommerce.web.app.exceptions.FavoritesCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.FavoritesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin("*")
@PreAuthorize("permitAll()")
public class FavoritesController {
    public final FavoritesService favoritesService;

    @GetMapping()
    public ResponseEntity<Page<FavoriteDetails>> show(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) throws FavoritesCustomException, UserNotFoundException, AuthenticationException {
            return ResponseEntity.ok(favoritesService.show(page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FavoriteDetails>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam SearchBuilderRequest searchBuilderRequest
    ) throws UserNotFoundException, AuthenticationException {
            return ResponseEntity.ok(favoritesService.search(searchBuilderRequest, page, size));
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<FavoritesResponse> add(@PathVariable(name = "postId") String postId) throws FavoritesCustomException {
            return ResponseEntity.ok(favoritesService.add(postId));
    }

    @DeleteMapping("/remove/{postId}")
    public ResponseEntity<Void> remove(@PathVariable(name = "postId") String postId) throws FavoritesCustomException {
            favoritesService.remove(postId);
            return ResponseEntity.ok().build();
    }
}
