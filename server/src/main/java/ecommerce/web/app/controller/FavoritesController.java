package ecommerce.web.app.controller;

import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.exceptions.FavoritesCustomException;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.service.FavoritesService;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.entities.Favorites;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
public class FavoritesController {

    public final PostService postService;
    public final UserService userService;
    public final FavoritesService favoritesService;
    public final MessageSource messageByLocale;

    public FavoritesController(PostService postService,
                               UserService userService,
                               FavoritesService favoritesService,
                               MessageSource messageByLocale){
        this.postService = postService;
        this.userService = userService;
        this.favoritesService = favoritesService;
        this.messageByLocale = messageByLocale;
    }

    private final Locale locale = Locale.ENGLISH;

    @GetMapping("/favorites/show")
    public ResponseEntity showFavorites() throws FavoritesCustomException {
        User authenticatedUser = userService.getAuthenticatedUser().get();
        List<Favorites> favoritesByUserAuthenticated =
                favoritesService.findFavoritesByUser(authenticatedUser);
        if(favoritesByUserAuthenticated.isEmpty()){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.noFavoritesFound",null,locale));
        }else {
            return new ResponseEntity(favoritesByUserAuthenticated,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/favorites/search")
    public ResponseEntity searchWishlist(@RequestParam String keyword) throws FavoritesCustomException {
        List<Favorites> searchForFavorites = favoritesService.searchWishlist(keyword);
        if(keyword.equals("") || keyword.equals(" ") ||
                keyword.equals(null)){
            return new ResponseEntity(favoritesService.findAll(),HttpStatus.ACCEPTED);
        }else if(searchForFavorites.isEmpty()){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        else {
            return new ResponseEntity(searchForFavorites,HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/favorites/add/{postId}")
    public ResponseEntity addToFavorites(@PathVariable(name = "postId") long postId)
            throws FavoritesCustomException {
        Optional<Post> findPost =  postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(!findPost.isPresent() || getAuthenticatedUser.equals("")){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }else {
            Post getFoundPost = findPost.get();
            return new ResponseEntity(favoritesService.addToFavorites(
                    getFoundPost,getAuthenticatedUser), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/favorites/remove/{postId}")
    public ResponseEntity removeFromFavorites(@PathVariable(name = "postId") long postId)
            throws FavoritesCustomException, PostCustomException {
        Optional<Post> findPost =  postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(!findPost.isPresent() || getAuthenticatedUser.equals("")){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }else {
            Post getFoundPost = findPost.get();
            Favorites findFavoritesByPostAndUser = favoritesService.findWishlistByUserAndPost(
                    getAuthenticatedUser,getFoundPost);
            if(findFavoritesByPostAndUser.equals("")){
                throw new FavoritesCustomException(
                        messageByLocale.getMessage("error.404.noFavoritesFound",null,locale));
            }else {
                favoritesService.deleteFavorites(findFavoritesByPostAndUser);
                return new ResponseEntity("Removed successfully from wishlist",
                        HttpStatus.ACCEPTED);
            }
        }

    }
}
