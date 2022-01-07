package ecommerce.web.app.controller;

import ecommerce.web.app.model.Card;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.service.CardService;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CardController {

    @Autowired
    CardService cardService;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @PostMapping("/add-to-card/{postId}")
    public ResponseEntity addToCard(@PathVariable(name = "postId") long postId){

            Post findPost =  postService.findByPostId(postId);

            User getAuthenticatedUser = userService.getAuthenticatedUser().get();

            return new ResponseEntity(cardService.addToCard(findPost,getAuthenticatedUser), HttpStatus.ACCEPTED);

    }

//    public ResponseEntity removeFromCard()
}