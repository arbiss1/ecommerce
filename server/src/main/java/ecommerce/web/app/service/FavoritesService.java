package ecommerce.web.app.service;

import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.entities.Favorites;
import ecommerce.web.app.repository.FavoritesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoritesService {

    public final FavoritesRepository favoritesRepository;

    public FavoritesService(FavoritesRepository favoritesRepository){
        this.favoritesRepository = favoritesRepository;
    }

    public Favorites addToFavorites(Post post, User user){
        Favorites favorites = new Favorites();
        favorites.setPost(post);
        favorites.setCreatedBy(user.getUsername());
        favorites.setCreatedDate(LocalDateTime.now());
        favorites.setLastModifiedBy(user.getUsername());
        favorites.setLastModifiedDate(LocalDateTime.now());
        favorites.setUser(user);
        return favoritesRepository.save(favorites);
    }

    public Favorites findWishlistByUserAndPost(User user , Post post){
        return favoritesRepository.findFavoritesByUserAndPost(user,post);
    }

    @Transactional
    public void deleteFavorites(Favorites favorites) {
//        return wishlistRepository.deleteWishlistByWishlistId(findWishlistByPostAndUser.getWishlistId());
        System.out.println(favorites.getId());
        favoritesRepository.removeFromFavorites(favorites.getId());
    }

    public List<Favorites> findFavoritesByUser(User authenticatedUser){
        return favoritesRepository.findFavoritesByUser(authenticatedUser);
    }

    public List<Favorites> searchWishlist(String keyword){
        return favoritesRepository.searchFavoritesByPostLike(keyword);
    }

    public List<Favorites> findAll() {
        return favoritesRepository.findAll();
    }
}
