package ecommerce.web.app.controller;

import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.Wishlist;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WishlistController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    WishlistService wishlistService;


    @GetMapping("/show-wishlist")
    public ResponseEntity showWishlist(){
        User authenticatedUser = userService.getAuthenticatedUser().get();

        List<Wishlist> wishlistByUserAuthenticated = wishlistService.findWishlistByUser(authenticatedUser);

        if(wishlistByUserAuthenticated.isEmpty()){
            return new ResponseEntity("Wishlist is empty",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(wishlistByUserAuthenticated,HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/add-to-wishlist/{postId}")
    public ResponseEntity addToWishlist(@PathVariable(name = "postId") long postId){

        Post findPost =  postService.findByPostId(postId);

        User getAuthenticatedUser = userService.getAuthenticatedUser().get();

        if(findPost.equals("") || getAuthenticatedUser.equals("")){
            return new ResponseEntity("No post or user authenticated found !",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(wishlistService.saveWishlist(findPost,getAuthenticatedUser), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/remove-from-wishlist/{postId}")
    public ResponseEntity removeFromWishlist(@PathVariable(name = "postId") long postId){
        Post findPost =  postService.findByPostId(postId);

        User getAuthenticatedUser = userService.getAuthenticatedUser().get();

        if(findPost.equals("") || getAuthenticatedUser.equals("")){
            return new ResponseEntity("No post or user authenticated found !",HttpStatus.NO_CONTENT);
        }else {
            Wishlist findWishlistByPostAndUser  = wishlistService.findWishlistByUserAndPost(getAuthenticatedUser,findPost);
            if(findWishlistByPostAndUser.equals("")){
                return new ResponseEntity("No wishlist found",HttpStatus.NO_CONTENT);
            }else {
                wishlistService.deleteWishlist(findWishlistByPostAndUser);
                return new ResponseEntity("Removed successfully from wishlist", HttpStatus.ACCEPTED);
            }
        }

    }
}
