package ecommerce.web.app.entity;

import ecommerce.web.app.domain.enums.PostStatus;
import ecommerce.web.app.domain.enums.AdvertIndex;
import ecommerce.web.app.domain.enums.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post")
@Data
public class Post extends BaseEntity {

    @NotEmpty(message = "Title must not be empty")
    private String title;
    @NotEmpty(message = "Description must not be empty")
    private String description;
    @NotEmpty(message = "Price must not be empty")
    private String price;
    private boolean isInSale;
    private String slug;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Advert Index must not be empty")
    private AdvertIndex postAdvertIndex;
    @NotBlank(message = "Address must not be null")
    private String address;
    @NotBlank(message = "Phone number must not be null")
    private String phoneNumber;
    @NotBlank(message = "First name must not be null")
    private String firstName;
    @NotBlank(message = "Last name must not be null")
    private String lastName;
    private String country;
    private String city;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;
    @Size(max = 5, min = 1)
    @NotNull(message = "Images must not be empty")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "post_image_uploads_mapped_list")
    private List<ImageUpload> imageUrls;
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subcategories_mapped_list")
    private List<Subcategory> subcategory;
    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

}
