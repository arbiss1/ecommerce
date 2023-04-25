package ecommerce.web.app.repository;

import ecommerce.web.app.entities.Card;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {


    @Query(nativeQuery = true,value = "SELECT sum(total_price) FROM ecommerce.card WHERE ordered_by_user = :user_id")
    int getTotalPrice(@Param(value = "user_id")long user_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "DELETE FROM ecommerce.card WHERE card_id = :cardId ;")
    void removeFromCard(@Param(value = "cardId") long cardId);

    Card findCardByUserAndPost(User getAuthenticatedUser, Post findPost);

    List<Card> findCardsByUser(User getAuthenticatedUser);
}
