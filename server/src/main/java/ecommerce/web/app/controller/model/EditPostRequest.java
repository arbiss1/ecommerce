package ecommerce.web.app.controller.model;

import ecommerce.web.app.controller.enums.Currency;
import ecommerce.web.app.enums.Fuel;
import ecommerce.web.app.enums.PostType;
import ecommerce.web.app.enums.Transmission;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NotNull(message = "You cannot edit null object")
public class EditPostRequest {
    private String title;
    private String description;
    private PostType postType;
    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.ALL;
    private String type;
    private String brand;
    private String model;
    private String color;
    private Transmission transmission;
    private String kilometers;
    private Fuel fuel;
    private String power;
    private String price;
    private String firstRegistration;
    private String engineSize;
    private List<String> imageUrls;
}
