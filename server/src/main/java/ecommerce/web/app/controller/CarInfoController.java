package ecommerce.web.app.controller;

import ecommerce.web.app.controller.model.CarBrands;
import ecommerce.web.app.controller.model.CarModels;
import ecommerce.web.app.controller.model.CarTypes;
import ecommerce.web.app.service.CarInfoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/car")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin("*")
@PreAuthorize("permitAll()")
public class CarInfoController {

    private final CarInfoService carInfoService;

    @GetMapping("/models")
    public ResponseEntity<CarModels> getCarModels(
            @RequestParam(name = "limit", defaultValue = "100") String limit,
            @RequestParam(name = "offset", defaultValue = "0") String offset,
            @RequestParam(name = "brand") String brand
    ) throws Exception {
        return ResponseEntity.ok(carInfoService.getCarModels(limit, offset, brand));
    }

    @GetMapping("/brands")
    public ResponseEntity<CarBrands> getCarBrands(){
        return ResponseEntity.ok(carInfoService.getCarBrands());
    }

    @GetMapping("/types")
    public ResponseEntity<CarTypes> getCarTypes(){
        return ResponseEntity.ok(carInfoService.getCarTypes());
    }
}
