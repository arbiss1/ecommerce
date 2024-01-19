package ecommerce.web.app.service;

import ecommerce.web.app.controller.model.FavoritesDetails;
import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.Favorites;
import ecommerce.web.app.exceptions.FavoritesCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.repository.FavoritesRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final MessageSource messageByLocale;
    private final PostService postService;
    private final UserService userService;
    private final Locale locale = Locale.ENGLISH;

    public FavoritesService(FavoritesRepository favoritesRepository, MessageSource messageByLocale, PostService postService, UserService userService){
        this.favoritesRepository = favoritesRepository;
        this.messageByLocale = messageByLocale;
        this.postService = postService;
        this.userService = userService;
    }

    public String addToFavorites(String postId) throws FavoritesCustomException {
        Optional<Post> optionalPost =  postService.findByPostId(postId);
        if(optionalPost.isEmpty()){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        Post post = optionalPost.get();
        Favorites favorites = new Favorites();
        favorites.setPost(post);
        favorites.setCreatedBy(post.getUser().getUsername());
        favorites.setCreatedAt(LocalDateTime.now());
        favorites.setModifiedBy(post.getUser().getUsername());
        favorites.setModifiedAt(LocalDateTime.now());
        return favoritesRepository.save(favorites).getId();
    }
    @Transactional
    public void deleteFavorites(String favoritesId) throws FavoritesCustomException {
        Optional<Favorites> findIfFavoriteExists = favoritesRepository.findById(favoritesId);
        if(findIfFavoriteExists.isEmpty()){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.noFavoritesFound",null,locale));
        }
        favoritesRepository.deleteById(favoritesId);
    }

    public List<FavoritesDetails> findFavoritesByUser() throws FavoritesCustomException, UserNotFoundException {
        List<Favorites> response = favoritesRepository.findFavoritesByUser(userService.getAuthenticatedUser());
        if(response.isEmpty()){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.noFavoritesFound",null,locale));
        }
        return mapToFavoritesDetails(response);
    }

    public List<FavoritesDetails> searchWishlist(String keyword) throws FavoritesCustomException {
        if(keyword.equals("") || keyword.equals(" ")){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        List<Favorites> response = favoritesRepository.searchFavoritesByPostLike(keyword);
        if(response.isEmpty()){
            throw new FavoritesCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        return mapToFavoritesDetails(response);
    }

    public List<FavoritesDetails> mapToFavoritesDetails(List<Favorites> favorites){
            return favorites.stream().map(favorite -> new FavoritesDetails(
                    favorite.getId(),
                    favorite.getPost().getId()
            )).collect(Collectors.toList());
    }
}
