package ecommerce.web.app.domain.post.model;

import ecommerce.web.app.domain.post.enums.PostStatus;
import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.domain.post.enums.AdvertIndex;
import ecommerce.web.app.domain.post.enums.Currency;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private long postId;

    @NotEmpty(message = "Title must not be empty")
    private String postTitle;

    @NotEmpty(message = "Description must not be empty")
    private String postDescription;

    @NotEmpty(message = "Price must not be empty")
    private String postPrice;
    private String postColor;
    private long postCode;

    private boolean isInSale;
    private String postSlug;
    private LocalDate postDate;
    private LocalTime postTime;
    //enum
    private Currency postCurrency;
    //enum
    @NotNull(message = "Advert Index must not be empty")
    private AdvertIndex postAdvertIndex;
    private String address;
    private long number;
    private String firstName;
    private String lastName;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;
    @Size(max = 5, min = 1)
    @NotEmpty(message = "Images must not be empty")
    @NotNull(message = "Images must not be empty")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_image_uploads")
    private List<ImageUpload> postImageUrl;
    private String postCategory;
    private String postSubcategory;
    //every new post will have pending status till its reviewed
    private PostStatus postStatus;
}
