package ecommerce.web.app.repository;

import ecommerce.web.app.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserId(String userId);

    Optional<Post> findById(String postId);

    @Query(nativeQuery = true,value = "SELECT * FROM ecommerce.post WHERE title LIKE %:keyword% " +
            "OR category LIKE %:keyword% OR subcategory LIKE %:keyword% OR description LIKE %:keyword%")
    List<Post> searchPosts(@Param("keyword") String keyword);

}
