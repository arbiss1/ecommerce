package ecommerce.web.app.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDetails {
    private String id;
    private String title;
    private String description;
    private String price;
}
