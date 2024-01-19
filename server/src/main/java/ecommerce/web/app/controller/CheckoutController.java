package ecommerce.web.app.controller;

import ecommerce.web.app.entities.ChargeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class CheckoutController {
    //TODO-to be impl
    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @PostMapping("/checkout")
    public ResponseEntity<ChargeRequest> checkout(@Valid @RequestBody ChargeRequest chargeRequest, BindingResult result) {
            return new ResponseEntity<ChargeRequest>(chargeRequest,HttpStatus.ACCEPTED);
        }
}
