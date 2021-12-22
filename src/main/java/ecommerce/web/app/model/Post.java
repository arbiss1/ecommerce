package ecommerce.web.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "post")
@Data
public class Post {

    public enum Currency{
        ALL,
        EUR,
        USD,
        GBP
    }

    public enum AdvertIndex{
        FREE,
        LOW_PLAN,
        HIGH_PLAN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;
    @NotEmpty(message = "Title must not be empty")
    private String postTitle;
    @NotEmpty(message = "Description must not be empty")
    private String postDescription;
    @NotEmpty(message = "Price must not be empty")
    private String postPrice;
    private String postColor;
    private String postQuantity;
    private String postCode;
    @NotEmpty(message = "IsInSale must not be empty")
    private boolean isInSale;
    @NotEmpty(message = "Slug must not be empty")
    private String postSlug;
    private LocalDate postDate;
    private LocalTime postTime;
    private Currency postCurrency;
    @NotNull(message = "Advert Index must not be empty")
    private AdvertIndex postAdvertIndex;
    private String address;
    private long number;
    private String firstName;
    private String lastName;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_category")
    @NotEmpty(message = "Category must not be empty")
    private Categories postCategories;
}
