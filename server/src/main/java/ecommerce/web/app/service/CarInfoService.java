package ecommerce.web.app.service;

import ecommerce.web.app.client.CarsApiClient;
import ecommerce.web.app.model.ManufacturerInfo;
import ecommerce.web.app.controller.model.CarBrands;
import ecommerce.web.app.controller.model.CarModels;
import ecommerce.web.app.controller.model.CarType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarInfoService {
    private final CarsApiClient carsApiClient;

    public CarModels getCarModels(String limit, String offset, String brand) {
        int limitValue = Integer.parseInt(limit);

        ManufacturerInfo manufacturerInfo = carsApiClient.getAllCarModels(limit, offset, brand);
        Set<String> response = manufacturerInfo.getResults().stream()
                .map(ManufacturerInfo.Result::getModel).sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        int totalCount = manufacturerInfo.getTotal_count();
        int totalPages = (totalCount / limitValue) + (totalCount % limitValue == 0 ? 0 : 1);

        for (int i = 1; i < totalPages; i++) {
            int newOffset = i + Integer.parseInt(offset);
            ManufacturerInfo nextBatch = carsApiClient.getAllCarModels(limit, String.valueOf(newOffset), brand);
            response.addAll(nextBatch.getResults().stream()
                    .map(ManufacturerInfo.Result::getModel).sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return new CarModels(response);
    }


    public CarBrands getCarBrands(){
        return new CarBrands(List.of( "Abarth",
                "Alfa Romeo",
                "Aston Martin",
                "Audi",
                "Bentley",
                "BMW",
                "Bugatti",
                "Cadillac",
                "Chevrolet",
                "Chrysler",
                "Citroën",
                "Dacia",
                "Daewoo",
                "Daihatsu",
                "Dodge",
                "Donkervoort",
                "DS",
                "Ferrari",
                "Fiat",
                "Fisker",
                "Ford",
                "Honda",
                "Hummer",
                "Hyundai",
                "Infiniti",
                "Iveco",
                "Jaguar",
                "Jeep",
                "Kia",
                "KTM",
                "Lada",
                "Lamborghini",
                "Lancia",
                "Land Rover",
                "Landwind",
                "Lexus",
                "Lotus",
                "Maserati",
                "Maybach",
                "Mazda",
                "McLaren",
                "Mercedes-Benz",
                "MG",
                "Mini",
                "Mitsubishi",
                "Morgan",
                "Nissan",
                "Opel",
                "Peugeot",
                "Porsche",
                "Renault",
                "Rolls-Royce",
                "Rover",
                "Saab",
                "Seat",
                "Skoda",
                "Smart",
                "SsangYong",
                "Subaru",
                "Suzuki",
                "Tesla",
                "Toyota",
                "Volkswagen",
                "Volvo"
        ));
    }

    public CarType getCarTypes(){
        return new CarType(List.of(
                "Convertible",
                "Hatchback",
                "SUV",
                "Coupe",
                "Truck",
                "Crossover",
                "Sports car",
                "Wagon",
                "Sedan"
        ));
    }
}
