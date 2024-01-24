package ecommerce.web.app.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
@Getter
@ToString
public class ApplicationEnv {
    private final String carsApiClientBaseUrl;

    public ApplicationEnv(@Value("${app.web.carsApiClient.baseUrl}") String carsApiClientBaseUrl){
        this.carsApiClientBaseUrl = carsApiClientBaseUrl;
    }
}
