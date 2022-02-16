package ecommerce.web.app.domain.service;

import ecommerce.web.app.domain.model.Card;
import ecommerce.web.app.domain.repository.CardRepository;
import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CardService {

    public final CardRepository cardRepository;

    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public Card addToCard(Post post, User authenticatedUser){
        Card card = new Card();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        card.setTime(time);
        card.setDate(date);
        card.setPost(post);
        card.setUser(authenticatedUser);
        card.setTotalPrice(post.getPrice());
        System.out.println(post.getPrice());
//        if (cardRepository.getTotalPrice() == "") {
//            System.out.println(0 + Integer.valueOf(post.getPostPrice()));
//        } else {
//            System.out.println(cardRepository.getTotalPrice() + Integer.valueOf(post.getPostPrice()));
//        }
        return cardRepository.save(card);
    }

    public void removeFromCard(Card card){
        cardRepository.removeFromCard(card.getCardId());
    }

    public Card findCardByUserAndPost(User getAuthenticatedUser, Post findPost) {
        return cardRepository.findCardByUserAndPost(getAuthenticatedUser,findPost);
    }

    public List<Card> findByUser(User getAuthenticatedUser) {
        return cardRepository.findCardsByUser(getAuthenticatedUser);
    }
}
