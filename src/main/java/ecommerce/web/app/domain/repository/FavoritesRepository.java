package ecommerce.web.app.domain.repository;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites,Long> {

//    Wishlist deleteWishlistByWishlistId(long wishlistId);

    Favorites findFavoritesByUserAndPost(User authenticatedUser, Post findPost);

    List<Favorites> findFavoritesByUser(User authenticatedUser);

    List<Favorites> searchFavoritesByPostLike(String keyword);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "DELETE FROM ecommerce.wishlist WHERE wishlist_id = :wishlistId ;")
    void removeFromFavorites(@Param(value = "wishlistId") long wishlistId);

}
