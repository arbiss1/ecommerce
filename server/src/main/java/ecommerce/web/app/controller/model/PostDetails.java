package ecommerce.web.app.controller.model;

import ecommerce.web.app.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDetails {
    private String id;
    private String title;
    private String description;
    private PostType postType;
    private Currency currency;
    private AdvertIndex postAdvertIndex;
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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> images;

    public PostDetails(String id, String title, String description, String price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
    }
}
