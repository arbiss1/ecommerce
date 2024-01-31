package ecommerce.web.app.service;

import ecommerce.web.app.client.CarsApiClient;
import ecommerce.web.app.model.ManufacturerInfo;
import ecommerce.web.app.controller.model.CarBrands;
import ecommerce.web.app.controller.model.CarModels;
import ecommerce.web.app.controller.model.CarTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarInfoService {
    private final CarsApiClient carsApiClient;

    @Cacheable("getCarModels")
    public CarModels getCarModels(String limit, String offset, String brand) throws Exception {
        int limitValue = Integer.parseInt(limit);
        int offsetValue = Integer.parseInt(offset);

        ManufacturerInfo initialBatch = carsApiClient.getAllCarModels(limit, offset, brand);
        Set<String> response = initialBatch.getResults().stream()
                .map(ManufacturerInfo.Result::getModel).sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));

        int totalCount = initialBatch.getTotal_count();
        int totalPages = (totalCount / limitValue) + (totalCount % limitValue == 0 ? 0 : 1);

        List<Future<Set<String>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 1; i < totalPages; i++) {
            int newOffset = i + offsetValue;
            Future<Set<String>> future = executorService.submit(() ->
                    carsApiClient.getAllCarModels(limit, String.valueOf(newOffset), brand)
                            .getResults().stream()
                            .map(ManufacturerInfo.Result::getModel).sorted()
                            .collect(Collectors.toCollection(LinkedHashSet::new)));

            futures.add(future);
        }

        for (Future<Set<String>> future : futures) {
            try {
                response.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new Exception("Error on retrieving car info");
            }
        }

        executorService.shutdown();

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

    public CarTypes getCarTypes(){
        return new CarTypes(List.of(
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
