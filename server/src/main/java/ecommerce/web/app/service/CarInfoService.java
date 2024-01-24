package ecommerce.web.app.service;

import ecommerce.web.app.client.CarsApiClient;
import ecommerce.web.app.client.model.ManufacturerInfo;
import ecommerce.web.app.controller.model.CarBrands;
import ecommerce.web.app.controller.model.CarModels;
import ecommerce.web.app.controller.model.CarType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarInfoService {
    private final CarsApiClient carsApiClient;

    public CarModels getCarModels(String limit, String offset, String brand, String keyword) {
        ManufacturerInfo response = carsApiClient.getAllCarModels(limit, offset, brand);
        for (int i = 1; i<= response.getTotal_count()/Integer.parseInt(limit) + 1; i++){
            if(response.getResults().stream().filter(result -> result.getModel().contains(keyword)).toList().isEmpty()){
                response = carsApiClient.getAllCarModels(limit, offset + i, brand);
            }
        }
        return new CarModels(response.getResults().stream().map(ManufacturerInfo.Result::getModel).toList().stream().filter(s -> s.contains(keyword)).collect(Collectors.toSet()));
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
                "Sedan",
                "Truck",
                "Crossover",
                "Sports car",
                "Wagon",
                "Sedan"
        ));
    }
}
