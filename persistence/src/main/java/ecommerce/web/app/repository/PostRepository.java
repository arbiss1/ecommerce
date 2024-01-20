package ecommerce.web.app.repository;

import ecommerce.web.app.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUserId(String userId);

    @Query(nativeQuery = true, value = "SELECT * FROM ecommerce_db.post " +
            "WHERE post.title LIKE CONCAT('%', :keyword, '%') " +
            "OR post.type LIKE CONCAT('%', :keyword, '%') " +
            "OR post.description LIKE CONCAT('%', :keyword, '%')")
    List<Post> searchByKeyword(@Param("keyword") String keyword);
}
