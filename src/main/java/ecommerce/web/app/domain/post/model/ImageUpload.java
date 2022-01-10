package ecommerce.web.app.domain.post.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "image_uploads")
@Data
public class ImageUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String imageUrl;
}
