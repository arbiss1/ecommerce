package ecommerce.web.app.domain.controller;

import ecommerce.web.app.entity.ChargeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class CheckoutController {
    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @RequestMapping("/checkout")
    public ResponseEntity checkout(@Valid @RequestBody ChargeRequest chargeRequest, BindingResult result) {

        if(result.hasErrors()){
            return new ResponseEntity(result, HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity(chargeRequest,HttpStatus.ACCEPTED);
        }
    }
}
