package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.FavoritesDetails;
import ecommerce.web.app.exceptions.FavoritesCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FavoritesController {
    public final FavoritesService favoritesService;

    @GetMapping()
    public ResponseEntity<List<FavoritesDetails>> show() throws FavoritesCustomException, UserNotFoundException, AuthenticationException {
            return ResponseEntity.ok(favoritesService.findFavoritesByUser());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FavoritesDetails>> search(@RequestParam String keyword) throws FavoritesCustomException {
            return ResponseEntity.ok(favoritesService.search(keyword));
    }

    @PostMapping("/add/{postId}")
    public ResponseEntity<String> addToFavorites(@PathVariable(name = "postId") String postId) throws FavoritesCustomException, UserNotFoundException {
            return ResponseEntity.ok(favoritesService.add(postId));
    }

    @DeleteMapping("/remove/{favoritesId}")
    public ResponseEntity<Void> remove(@PathVariable(name = "favoritesId") String favoritesId) throws FavoritesCustomException {
            favoritesService.remove(favoritesId);
            return ResponseEntity.ok().build();
    }
}
