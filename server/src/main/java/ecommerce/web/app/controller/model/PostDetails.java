package ecommerce.web.app.controller.model;

import ecommerce.web.app.entities.Category;
import ecommerce.web.app.entities.ImageUpload;
import ecommerce.web.app.entities.Subcategory;
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
    private Category category;
    private List<Subcategory> subcategory;
}
