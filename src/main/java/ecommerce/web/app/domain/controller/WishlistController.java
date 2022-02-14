package ecommerce.web.app.domain.controller;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.service.PostService;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.domain.service.WishlistService;
import ecommerce.web.app.domain.model.Wishlist;
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
