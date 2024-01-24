package ecommerce.web.app.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostDetails {
    private String id;
    private String title;
    private String description;
    private String price;
    private List<String> images;

    public PostDetails(String id, String title, String description, String price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
    }
}
