package ecommerce.web.app.domain.post.repository;

import ecommerce.web.app.domain.post.model.ImageUpload;
import ecommerce.web.app.domain.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByUserId(long userId);

    Post findByPostId(long postId);


    @Transactional
    Post deleteByPostImageUrl(ImageUpload imageUrls);

}
