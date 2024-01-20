package ecommerce.web.app.controller.model;

import ecommerce.web.app.enums.PostType;
import lombok.Data;

@Data
public class SearchBuilderRequest {
    private PostType postType;
    private String type;
    private String brand;
    private String color;
    private String transmission;
    private String kilometers;
    private String fuel;
    private String power;
    private String lowestPrice;
    private String highestPrice;
    private String firstRegistration;
    private String engineSize;
    private String fromYear;
    private String toYear;
}
