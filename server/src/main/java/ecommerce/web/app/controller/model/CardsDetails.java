package ecommerce.web.app.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardsDetails {
    private String id;
    private String postId;

    private String totalPrice;
}
