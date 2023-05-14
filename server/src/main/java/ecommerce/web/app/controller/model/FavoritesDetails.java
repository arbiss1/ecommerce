package ecommerce.web.app.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoritesDetails {
    private String favoritesId;
    private String postId;
}
