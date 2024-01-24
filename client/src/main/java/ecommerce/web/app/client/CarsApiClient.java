package ecommerce.web.app.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ecommerce.web.app.model.ManufacturerInfo;
import ecommerce.web.app.config.ApplicationEnv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@AllArgsConstructor
public class CarsApiClient {
    private ApplicationEnv applicationEnv;
    private final ObjectMapper objectMapper;

    public ManufacturerInfo getAllCarModels(String limit, String offset, String brand) {
        try {
            URL apiUrl = new URL(applicationEnv.getCarsApiClientBaseUrl() + "?limit=" + limit + "&offset=" + (Integer.parseInt(offset) * Integer.parseInt(limit)) + "&refine=make%3A" + "'" + brand + "'");

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return objectMapper.readerFor(ManufacturerInfo.class).readValue(response.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
