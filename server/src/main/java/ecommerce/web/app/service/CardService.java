package ecommerce.web.app.service;

import ecommerce.web.app.controller.model.CardResponse;
import ecommerce.web.app.controller.model.CardsDetails;
import ecommerce.web.app.entities.Card;
import ecommerce.web.app.exceptions.PostCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.repository.CardRepository;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.User;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    public final CardRepository cardRepository;
    public final PostService postService;
    public final UserService userService;
    public final MessageSource messageByLocale;
    private final Locale locale = Locale.ENGLISH;


    public CardService(CardRepository cardRepository, PostService postService, UserService userService, MessageSource messageByLocale){
        this.postService = postService;
        this.userService = userService;
        this.cardRepository = cardRepository;
        this.messageByLocale = messageByLocale;
    }

    public CardResponse addToCard(String postId) throws PostCustomException, UserNotFoundException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if(findPost.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        Post getFoundPost = findPost.get();
        User getAuthenticatedUser = userService.getAuthenticatedUser();
        Card findIfPostAlreadyExists = cardRepository.findCardByUserAndPost
                (getAuthenticatedUser,getFoundPost);
        if(!(findIfPostAlreadyExists == null)){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.409.postExistsToCard",null,locale));
        }
        Card card = new Card();
        card.setCreatedBy(getAuthenticatedUser.getUsername());
        card.setCreatedDate(LocalDateTime.now());
        card.setLastModifiedDate(LocalDateTime.now());
        card.setLastModifiedBy(getAuthenticatedUser.getUsername());
        card.setPost(getFoundPost);
        card.setTotalPrice(getFoundPost.getPrice());
        System.out.println(getFoundPost.getPrice());
        return new CardResponse(cardRepository.save(card).getId());
    }

    public void removeFromCard(String postId) throws PostCustomException, UserNotFoundException {
        Optional<Post> findPost = postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser();
        if (findPost.isEmpty() || getAuthenticatedUser.getUsername().equals("")) {
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.userOrPostNotFound", null, locale));
        } else {
            Post getFoundPost = findPost.get();
            Card card = cardRepository.findCardByUserAndPost
                    (getAuthenticatedUser, getFoundPost);
            cardRepository.removeFromCard(card.getId());
        }
    }

    public List<CardsDetails> findCardsByUser() throws UserNotFoundException {
        List<Card> response = cardRepository.findCardsByUser(userService.getAuthenticatedUser());
        return mapToCardsDetails(response);
    }

    public List<CardsDetails> mapToCardsDetails(List<Card> cards){
        return cards.stream().map(card -> new CardsDetails(
                card.getId(),
                card.getPost().getId(),
                card.getTotalPrice()
        )).collect(Collectors.toList());
    }
}
