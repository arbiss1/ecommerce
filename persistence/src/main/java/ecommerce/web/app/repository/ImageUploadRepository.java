package ecommerce.web.app.repository;

import ecommerce.web.app.entities.ImageUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUploadRepository extends JpaRepository<ImageUpload,Long> {

    ImageUpload deleteById(long id);
}
