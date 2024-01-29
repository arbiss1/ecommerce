package ecommerce.web.app.repository;

import ecommerce.web.app.entities.Post;
import ecommerce.web.app.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface PostRepository extends PagingAndSortingRepository<Post, String> {
    Page<Post> findByUserId(String userId, Pageable pageable);
    Post save(Post post);
    Optional<Post> findById(String postId);
    void deleteById(String postId);
    Page<Post> findAllByUserAndIsFavoriteTrue(Pageable pageable, User user);
}
