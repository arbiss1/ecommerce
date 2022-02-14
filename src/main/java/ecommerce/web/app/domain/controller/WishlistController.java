package ecommerce.web.app.domain.controller;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.service.PostService;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.domain.service.WishlistService;
import ecommerce.web.app.domain.model.Wishlist;
import ecommerce.web.app.exception.FavoritesCustomException;
import ecommerce.web.app.i18n.MessageByLocaleImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class WishlistController {

    public final PostService postService;
    public final UserService userService;
    public final WishlistService wishlistService;
    public final MessageByLocaleImpl messageByLocale;

    public WishlistController(PostService postService, UserService userService,
                              WishlistService wishlistService,MessageByLocaleImpl messageByLocale){
        this.postService = postService;
        this.userService = userService;
        this.wishlistService = wishlistService;
        this.messageByLocale = messageByLocale;
    }



    @GetMapping("/show-wishlist")
    public ResponseEntity showWishlist(){
        User authenticatedUser = userService.getAuthenticatedUser().get();
        List<Wishlist> wishlistByUserAuthenticated =
                wishlistService.findWishlistByUser(authenticatedUser);
        if(wishlistByUserAuthenticated.isEmpty()){
            return new ResponseEntity("Wishlist is empty",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(wishlistByUserAuthenticated,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/search-wishlist")
    public ResponseEntity searchWishlist(@RequestParam String keyword){
        List<Wishlist> searchForWishlists = wishlistService.searchWishlist(keyword);
        if(keyword.equals("") || keyword.equals(" ") ||
                keyword.equals(null)){
            return new ResponseEntity(wishlistService.findAll(),HttpStatus.ACCEPTED);
        }else if(searchForWishlists.isEmpty()){
            return new ResponseEntity("No posts found",HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity(searchForWishlists,HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/add-to-wishlist/{postId}")
    public ResponseEntity addToWishlist(@PathVariable(name = "postId") String postId)
            throws FavoritesCustomException {
        Optional<Post> findPost =  postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(!findPost.isPresent() || getAuthenticatedUser.equals("")){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            Post getFoundPost = findPost.get();
            return new ResponseEntity(wishlistService.saveWishlist(getFoundPost,getAuthenticatedUser), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/remove-from-wishlist/{postId}")
    public ResponseEntity removeFromWishlist(@PathVariable(name = "postId") String postId)
            throws FavoritesCustomException {
        Optional<Post> findPost =  postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(!findPost.isPresent() || getAuthenticatedUser.equals("")){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            Post getFoundPost = findPost.get();
            Wishlist findWishlistByPostAndUser  = wishlistService.findWishlistByUserAndPost(
                    getAuthenticatedUser,getFoundPost);
            if(findWishlistByPostAndUser.equals("")){
                return new ResponseEntity("No wishlist found",HttpStatus.NO_CONTENT);
            }else {
                wishlistService.deleteWishlist(findWishlistByPostAndUser);
                return new ResponseEntity("Removed successfully from wishlist", HttpStatus.ACCEPTED);
            }
        }

    }
}
