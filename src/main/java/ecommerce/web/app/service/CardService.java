package ecommerce.web.app.service;

import ecommerce.web.app.model.Card;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.Wishlist;
import ecommerce.web.app.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    public Card addToCard(Post post, User authenticatedUser){
        Card card = new Card();
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        card.setTime(time);
        card.setDate(date);
        card.setPost(post);
        card.setUser(authenticatedUser);
        card.setTotalPrice(post.getPostPrice());
        System.out.println(post.getPostPrice());
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
