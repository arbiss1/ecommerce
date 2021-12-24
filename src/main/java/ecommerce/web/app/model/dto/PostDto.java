package ecommerce.web.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecommerce.web.app.model.Categories;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.enums.AdvertIndex;
import ecommerce.web.app.model.enums.Currency;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PostDto {

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
    //    @NotEmpty(message = "IsInSale must not be empty")
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
    @JsonProperty("user")
    private UserGetDto user;
    private String postCategories;
    private String subPostCategory;
    private List<String> postImageUrl;
}
