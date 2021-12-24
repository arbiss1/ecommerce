package ecommerce.web.app.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "image_upload")
@Data
public class ImageUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String imageUrl;
}
