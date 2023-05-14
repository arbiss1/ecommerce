package ecommerce.web.app.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import ecommerce.web.app.entities.ChargeRequest;
import ecommerce.web.app.exceptions.ChargeCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.service.StripeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

@Controller
public class ChargeController {

    private final StripeService paymentsService;

    public ChargeController(StripeService paymentsService){
        this.paymentsService = paymentsService;
    }

    @PostMapping("/charge")
    public ResponseEntity<Charge> charge(@Valid @RequestBody ChargeRequest chargeRequest, BindingResult result) throws StripeException, UserNotFoundException, ChargeCustomException {
        return ResponseEntity.ok(paymentsService.charge(chargeRequest, result));
    }
}
