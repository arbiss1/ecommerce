package ecommerce.web.app.domain.post.repository;

import ecommerce.web.app.domain.post.model.ImageUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUploadRepository extends JpaRepository<ImageUpload,Long> {

    ImageUpload deleteById(long id);
}
