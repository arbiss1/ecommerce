package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.CarBrands;
import ecommerce.web.app.controller.model.CarModels;
import ecommerce.web.app.controller.model.CarType;
import ecommerce.web.app.service.CarInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin("*")
public class CarInfoController {

    private final CarInfoService carInfoService;

    @GetMapping("/models")
    public ResponseEntity<CarModels> getCarModels(
            @RequestParam(name = "limit", defaultValue = "100") String limit,
            @RequestParam(name = "offset", defaultValue = "0") String offset,
            @RequestParam(name = "brand") String brand,
            @RequestParam(name = "keyword", required = false) String keyword
    ){
        return ResponseEntity.ok(carInfoService.getCarModels(limit, offset, brand, keyword));
    }

    @GetMapping("/brands")
    public ResponseEntity<CarBrands> getCarBrands(){
        return ResponseEntity.ok(carInfoService.getCarBrands());
    }

    @GetMapping("/types")
    public ResponseEntity<CarType> getCarTypes(){
        return ResponseEntity.ok(carInfoService.getCarTypes());
    }
}
