package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.FavoritesDetails;
import ecommerce.web.app.exceptions.FavoritesCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class FavoritesController {

    public final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService){
        this.favoritesService = favoritesService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoritesDetails>> showFavorites() throws FavoritesCustomException, UserNotFoundException {
            return ResponseEntity.ok(favoritesService.findFavoritesByUser());
    }

    @GetMapping("/favorites/search")
    public ResponseEntity<List<FavoritesDetails>> searchWishlist(@RequestParam String keyword) throws FavoritesCustomException {
            return ResponseEntity.ok(favoritesService.searchWishlist(keyword));
    }

    @PostMapping("/favorites/add/{postId}")
    public ResponseEntity<String> addToFavorites(@PathVariable(name = "postId") String postId) throws FavoritesCustomException, UserNotFoundException {
            return ResponseEntity.ok(favoritesService.addToFavorites(postId));
    }

    @DeleteMapping("/favorites/remove/{favoritesId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable(name = "favoritesId") String favoritesId) throws FavoritesCustomException {
            favoritesService.deleteFavorites(favoritesId);
            return ResponseEntity.ok().build();
    }
}
