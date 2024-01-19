package ecommerce.web.app.entities;

import ecommerce.web.app.utils.ImageUtil;
import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "image_uploads")
@Data
public class ImageUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Lob
    @Column(name = "profile_image", columnDefinition = "MEDIUMBLOB")
    private byte[] profileImage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post ;

    public void setProfileImage(String profileImage) {
        this.profileImage = ImageUtil.compressFile(profileImage);
    }

    public String getProfileImage() {
        return ImageUtil.decompressFile(profileImage);
    }
}
