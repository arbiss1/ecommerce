package ecommerce.web.app.domain.service;

import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.User;
import ecommerce.web.app.domain.model.Wishlist;
import ecommerce.web.app.domain.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {

    @Autowired
    WishlistRepository wishlistRepository;

    public Wishlist saveWishlist(Post post, User user){
        Wishlist wishlist = new Wishlist();
        wishlist.setPost(post);
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    public Wishlist findWishlistByUserAndPost(User user , Post post){
        return wishlistRepository.findWishlistByUserAndPost(user,post);
    }

    @Transactional
    public void deleteWishlist(Wishlist wishlist) {
//        return wishlistRepository.deleteWishlistByWishlistId(findWishlistByPostAndUser.getWishlistId());
        System.out.println(wishlist.getWishlistId());
        wishlistRepository.removeFromWishlist(wishlist.getWishlistId());
    }

    public List<Wishlist> findWishlistByUser(User authenticatedUser){
        return wishlistRepository.findWishlistByUser(authenticatedUser);
    }

    public List<Wishlist> searchWishlist(String keyword){
        return wishlistRepository.searchWishlistByPostLike(keyword);
    }

    public List<Wishlist> findAll() {
        return wishlistRepository.findAll();
    }
}
