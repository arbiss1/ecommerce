package ecommerce.web.app.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModels {
    private Set<String> models;
}
