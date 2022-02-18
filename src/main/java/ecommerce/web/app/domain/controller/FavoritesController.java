package ecommerce.web.app.domain.controller;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.service.PostService;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.domain.service.FavoritesService;
import ecommerce.web.app.domain.model.Favorites;
import ecommerce.web.app.exception.FavoritesCustomException;
import ecommerce.web.app.exception.PostCustomException;
import ecommerce.web.app.i18nConfig.MessageByLocaleImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class FavoritesController {

    public final PostService postService;
    public final UserService userService;
    public final FavoritesService favoritesService;
    public final MessageByLocaleImpl messageByLocale;

    public FavoritesController(PostService postService,
                               UserService userService,
                               FavoritesService favoritesService,
                               MessageByLocaleImpl messageByLocale){
        this.postService = postService;
        this.userService = userService;
        this.favoritesService = favoritesService;
        this.messageByLocale = messageByLocale;
    }



    @GetMapping("/show-favorites")
    public ResponseEntity showWishlist(){
        User authenticatedUser = userService.getAuthenticatedUser().get();
        List<Favorites> favoritesByUserAuthenticated =
                favoritesService.findWishlistByUser(authenticatedUser);
        if(favoritesByUserAuthenticated.isEmpty()){
            return new ResponseEntity("Wishlist is empty",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(favoritesByUserAuthenticated,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/search-favorites")
    public ResponseEntity searchWishlist(@RequestParam String keyword){
        List<Favorites> searchForFavorites = favoritesService.searchWishlist(keyword);
        if(keyword.equals("") || keyword.equals(" ") ||
                keyword.equals(null)){
            return new ResponseEntity(favoritesService.findAll(),HttpStatus.ACCEPTED);
        }else if(searchForFavorites.isEmpty()){
            return new ResponseEntity("No posts found",HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity(searchForFavorites,HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/add-to-favorites/{postId}")
    public ResponseEntity addToWishlist(@PathVariable(name = "postId") String postId)
            throws FavoritesCustomException {
        Optional<Post> findPost =  postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(!findPost.isPresent() || getAuthenticatedUser.equals("")){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            Post getFoundPost = findPost.get();
            return new ResponseEntity(favoritesService.saveWishlist(
                    getFoundPost,getAuthenticatedUser), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/remove-from-favorites/{postId}")
    public ResponseEntity removeFromWishlist(@PathVariable(name = "postId") String postId)
            throws FavoritesCustomException, PostCustomException {
        Optional<Post> findPost =  postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(!findPost.isPresent() || getAuthenticatedUser.equals("")){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            Post getFoundPost = findPost.get();
            Favorites findFavoritesByPostAndUser = favoritesService.findWishlistByUserAndPost(
                    getAuthenticatedUser,getFoundPost);
            if(findFavoritesByPostAndUser.equals("")){
                throw new FavoritesCustomException(
                        messageByLocale.getMessage("error.404.noFavoritesFound"));
            }else {
                favoritesService.deleteWishlist(findFavoritesByPostAndUser);
                return new ResponseEntity("Removed successfully from wishlist",
                        HttpStatus.ACCEPTED);
            }
        }

    }
}
