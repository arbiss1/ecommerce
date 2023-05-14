package ecommerce.web.app.repository;

import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.entities.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, String> {

//    Wishlist deleteWishlistByWishlistId(long wishlistId);

    Favorites findFavoritesByUserAndPost(User authenticatedUser, Post findPost);

    List<Favorites> findFavoritesByUser(User authenticatedUser);

    List<Favorites> searchFavoritesByPostLike(String keyword);

}
