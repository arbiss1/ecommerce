package ecommerce.web.app.repository;

import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageUploadRepository extends JpaRepository<ImageUpload, String> {
    List<ImageUpload> findAllByPost(Post post);

    void deleteAllByPost(Post post);
}
