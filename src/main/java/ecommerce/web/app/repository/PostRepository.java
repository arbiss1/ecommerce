package ecommerce.web.app.repository;

import ecommerce.web.app.model.ImageUpload;
import ecommerce.web.app.model.Post;
import javafx.geometry.Pos;
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

}
