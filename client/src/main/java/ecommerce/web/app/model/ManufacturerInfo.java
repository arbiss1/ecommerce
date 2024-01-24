package ecommerce.web.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManufacturerInfo {
    private Integer total_count;
    private List<Result> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private String make;
        private String model;
    }
}
