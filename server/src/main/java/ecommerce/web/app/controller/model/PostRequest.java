package ecommerce.web.app.controller.model;

import ecommerce.web.app.enums.Fuel;
import ecommerce.web.app.enums.PostType;
import ecommerce.web.app.controller.enums.AdvertIndex;
import ecommerce.web.app.controller.enums.Currency;
import ecommerce.web.app.enums.Transmission;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostRequest {
    @NotEmpty(message = "Title must not be empty")
    private String title;
    @NotEmpty(message = "Description must not be empty")
    private String description;
    private PostType postType;
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.ALL;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Advert Index must not be empty")
    private AdvertIndex postAdvertIndex = AdvertIndex.FREE;
    @NotEmpty(message = "Car type must not be empty")
    private String type;
    @NotEmpty(message = "Brand must not be empty")
    private String brand;
    @NotEmpty(message = "Model must not be empty")
    private String model;
    @NotEmpty(message = "Color must not be empty")
    private String color;
    @NotEmpty(message = "Transmission must not be empty")
    private Transmission transmission;
    @NotEmpty(message = "Kilometers must not be empty")
    private String kilometers;
    @NotEmpty(message = "Fuel must not be empty")
    private Fuel fuel;
    @NotEmpty(message = "Power must not be empty")
    private String power;
    @NotEmpty(message = "Price must not be empty")
    private String price;
    @NotEmpty(message = "First registration must not be empty")
    private String firstRegistration;
    @NotEmpty(message = "Engine size must not be empty")
    private String engineSize;
    private List<String> imageUrls;
}
