package ecommerce.web.app.repository;

import ecommerce.web.app.model.Card;
import ecommerce.web.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    public List<Card> findAllByUser(User authenticatedUser);

    @Query(nativeQuery = true,value = "SELECT sum(total_price) FROM ecommerce.card")
    long getTotalPrice();
}
