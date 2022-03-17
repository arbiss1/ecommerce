package ecommerce.web.app.domain.controller;

import ecommerce.web.app.domain.service.CardService;
import ecommerce.web.app.entity.Card;
import ecommerce.web.app.entity.Post;
import ecommerce.web.app.entity.User;
import ecommerce.web.app.domain.service.PostService;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.exception.customExceptions.CardCustomException;
import ecommerce.web.app.exception.customExceptions.PostCustomException;
import ecommerce.web.app.exception.customExceptions.UserNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;
import java.util.Optional;

@RestController
public class CardController {

    public final CardService cardService;
    public final PostService postService;
    public final UserService userService;
    public final MessageSource messageByLocale;

    public CardController(CardService cardService,
                          PostService postService,
                          UserService userService,
                          MessageSource messageByLocale){
        this.cardService = cardService;
        this.postService = postService;
        this.userService = userService;
        this.messageByLocale = messageByLocale;
    }

    private final Locale locale = Locale.ENGLISH;

    @PostMapping("/card/add/{postId}")
    public ResponseEntity addToCard(@PathVariable(name = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if(!findPost.isPresent()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        Post getFoundPost = findPost.get();
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        Card findIfPostAlreadyExists = cardService.findCardByUserAndPost
                (getAuthenticatedUser,getFoundPost);
        if(!(findIfPostAlreadyExists == null)){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.409.postExistsToCard",null,locale));
        }else {
            return new ResponseEntity(cardService.addToCard(getFoundPost, getAuthenticatedUser),
                    HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/card/remove/{postId}")
    public ResponseEntity removeFromCard(@PathVariable(name = "postId") long postId)
            throws PostCustomException, CardCustomException {
        Optional<Post> findPost = postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if (!findPost.isPresent() || getAuthenticatedUser.equals("")) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.userOrPostNotFound",null,locale));
        } else {
            Post getFoundPost = findPost.get();
            Card findCardByPostAndUser = cardService.findCardByUserAndPost
                    (getAuthenticatedUser, getFoundPost);
            if (findCardByPostAndUser.equals("")) {
                throw new CardCustomException(
                        messageByLocale.getMessage("error.404.itemsOnCardNotFound",null,locale));
            } else {
                cardService.removeFromCard(findCardByPostAndUser);
                return new ResponseEntity("Removed successfully from wishlist",
                        HttpStatus.ACCEPTED);
            }
        }
    }

    @GetMapping("/card/show")
    public ResponseEntity showCard() throws UserNotFoundException {
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(getAuthenticatedUser == null || getAuthenticatedUser == null){
            throw new UserNotFoundException(
                    messageByLocale.getMessage("error.409.noAuthenticatedUser",null,locale));
        }
        else if(cardService.findByUser(getAuthenticatedUser).isEmpty() ||
                cardService.findByUser(getAuthenticatedUser) == null){
            return new ResponseEntity("Card is empty",HttpStatus.NO_CONTENT);
        }
        else {
            System.out.println(cardService.findByUser(getAuthenticatedUser).stream().count());
            return new ResponseEntity(cardService.findByUser(getAuthenticatedUser),HttpStatus.ACCEPTED);
        }
    }
}