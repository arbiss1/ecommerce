package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.CardResponse;
import ecommerce.web.app.controller.model.CardsDetails;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {

    public final CardService cardService;

    public CardController(CardService cardService){
        this.cardService = cardService;
    }

    @PostMapping("/card/add/{postId}")
    public ResponseEntity<CardResponse> addToCard(@PathVariable(name = "postId") String postId) throws PostCustomException, UserNotFoundException {
            return ResponseEntity.ok(cardService.addToCard(postId));
    }

    @DeleteMapping("/card/remove/{postId}")
    public ResponseEntity<Void> removeFromCard(@PathVariable(name = "postId") String postId)
            throws PostCustomException, UserNotFoundException {
                cardService.removeFromCard(postId);
                return ResponseEntity.ok().build();
    }

    @GetMapping("/card/show")
    public ResponseEntity<List<CardsDetails>> showCard() throws UserNotFoundException {
            return ResponseEntity.ok(cardService.findCardsByUser());
    }
}