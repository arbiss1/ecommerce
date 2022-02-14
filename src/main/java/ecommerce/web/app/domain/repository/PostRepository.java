package ecommerce.web.app.domain.repository;

import ecommerce.web.app.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,String> {
    List<Post> findByUserId(long userId);

    Optional<Post> findById(String postId);

//    @Transactional
//    Post deleteByPostImageUrl(ImageUpload imageUrls);

//    Page<Post> findAll(Pageable pageable);

    @Query(nativeQuery = true,value = "SELECT * FROM ecommerce.post WHERE post_title LIKE %:keyword% " +
            "OR post_category LIKE %:keyword% OR post_subcategory LIKE %:keyword%")
    List<Post> searchPosts(@Param("keyword") String keyword);

}
