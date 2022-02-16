package ecommerce.web.app.domain.service;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.Favorites;
import ecommerce.web.app.domain.repository.FavoritesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoritesService {

    public final FavoritesRepository favoritesRepository;

    public FavoritesService(FavoritesRepository favoritesRepository){
        this.favoritesRepository = favoritesRepository;
    }

    public Favorites saveWishlist(Post post, User user){
        Favorites favorites = new Favorites();
        favorites.setPost(post);
        favorites.setUser(user);
        return favoritesRepository.save(favorites);
    }

    public Favorites findWishlistByUserAndPost(User user , Post post){
        return favoritesRepository.findWishlistByUserAndPost(user,post);
    }

    @Transactional
    public void deleteWishlist(Favorites favorites) {
//        return wishlistRepository.deleteWishlistByWishlistId(findWishlistByPostAndUser.getWishlistId());
        System.out.println(favorites.getWishlistId());
        favoritesRepository.removeFromWishlist(favorites.getWishlistId());
    }

    public List<Favorites> findWishlistByUser(User authenticatedUser){
        return favoritesRepository.findWishlistByUser(authenticatedUser);
    }

    public List<Favorites> searchWishlist(String keyword){
        return favoritesRepository.searchWishlistByPostLike(keyword);
    }

    public List<Favorites> findAll() {
        return favoritesRepository.findAll();
    }
}
