package ecommerce.web.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecommerce.web.app.model.enums.AdvertIndex;
import ecommerce.web.app.model.enums.Currency;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PostDto {

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
    @JsonProperty("user")
    private UserGetDto user;
    private String postCategories;
    private String subPostCategory;
    private List<String> postImageUrl;
}
