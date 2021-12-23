package ecommerce.web.app.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    private String postTitle;
    private String postDescription;
    private String postPrice;
    private String postColor;
    private String postQuantity;
    private String postCode;
    private boolean isInSale;
    private String postSlug;
    private LocalDate postDate;
    private LocalTime postTime;
    private Currency postCurrency;
    private AdvertIndex postAdvertIndex;
    private String address;
    private long number;
    private String firstName;
    private String lastName;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "post_category")
//    @NotEmpty(message = "Category must not be empty")
    private String postCategories;
    private String subPostCategory;
    private String postImageUrl;

}
