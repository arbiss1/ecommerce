package ecommerce.web.app.entities;

import ecommerce.web.app.enums.*;
import lombok.Data;
import jakarta.persistence.*;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private PostType postType;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private AdvertIndex postAdvertIndex;
    private String type;
    private String brand;
    private String color;
    private String transmission;
    private String kilometers;
    private String fuel;
    private String power;
    private String price;
    private String firstRegistration;
    private String engineSize;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;
    @CreatedDate
    private LocalDateTime createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @LastModifiedBy
    private String modifiedBy;
}
