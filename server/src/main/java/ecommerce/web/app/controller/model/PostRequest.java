package ecommerce.web.app.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecommerce.web.app.controller.enums.AdvertIndex;
import ecommerce.web.app.controller.enums.Currency;
import ecommerce.web.app.entities.Category;
import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Subcategory;
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
