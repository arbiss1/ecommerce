package ecommerce.web.app.model;

import ecommerce.web.app.model.enums.AdvertIndex;
import ecommerce.web.app.model.enums.Currency;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
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
    //enum
    private Currency postCurrency;
    //enum
    private AdvertIndex postAdvertIndex;
    private String address;
    private long number;
    private String firstName;
    private String lastName;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "category_subcategory_mapping",
//            joinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "post_id")},
//            inverseJoinColumns = {@JoinColumn(name = "subcategory_id", referencedColumnName = "post_id")})
//    @MapKeyJoinColumn(name = "details_id")
//    Map<Categories, SubCategories> details;
//    @ManyToMany(cascade = CascadeType.ALL)
//    private List<ImageUpload> postImageUrl;

    private String postImageUrl;

}
