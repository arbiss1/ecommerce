package ecommerce.web.app.domain.wishlist.service;

import ecommerce.web.app.domain.post.model.Post;
import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.domain.wishlist.model.Wishlist;
import ecommerce.web.app.domain.wishlist.repository.WishlistRepository;
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
}