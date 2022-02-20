package ecommerce.web.app.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ecommerce.web.app.domain.enums.AdvertIndex;
import ecommerce.web.app.domain.enums.Currency;
import ecommerce.web.app.domain.model.ImageUpload;
import lombok.Data;

import javax.persistence.JoinColumn;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private String category;
    @JsonProperty("subCategory")
    private String subCategory;
    @JsonProperty("imageUrls")
    private List<ImageUpload> imageUrls;
}
