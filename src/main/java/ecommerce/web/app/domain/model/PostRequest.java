package ecommerce.web.app.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecommerce.web.app.domain.enums.AdvertIndex;
import ecommerce.web.app.domain.enums.Currency;
import ecommerce.web.app.entity.Category;
import ecommerce.web.app.entity.ImageUpload;
import ecommerce.web.app.entity.Subcategory;
import lombok.Data;

import java.util.List;

@Data
public class PostRequest {

    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price")
    private String price;
    @JsonProperty("isInSale")
    private boolean isInSale;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("currency")
    private Currency currency;
    @JsonProperty("advertIndex")
    private AdvertIndex advertIndex;
    @JsonProperty("category")
    private Category category;
    @JsonProperty("subCategory")
    private List<Subcategory> subCategory;
    @JsonProperty("imageUrls")
    private List<ImageUpload> imageUrls;
}
