package ecommerce.web.app.domain.post.repository;

import ecommerce.web.app.domain.post.model.ImageUpload;
import ecommerce.web.app.domain.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByUserId(long userId);

    Post findByPostId(long postId);

    @Transactional
    Post deleteByPostImageUrl(ImageUpload imageUrls);

//    Page<Post> findAll(Pageable pageable);

    @Query(nativeQuery = true,value = "SELECT * FROM ecommerce.post WHERE post_title LIKE %:keyword% " +
            "OR post_category LIKE %:keyword% OR post_subcategory LIKE %:keyword%")
    List<Post> searchPosts(@Param("keyword") String keyword);

}
