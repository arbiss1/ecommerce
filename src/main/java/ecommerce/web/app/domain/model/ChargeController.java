package ecommerce.web.app.domain.model;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import ecommerce.web.app.domain.service.StripeService;
import ecommerce.web.app.domain.repository.CardRepository;
import ecommerce.web.app.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

@Controller
public class ChargeController {

    @Autowired
    private StripeService paymentsService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserService userService;

//    @Value("${STRIPE_PUBLIC_KEY}")
//    private String stripePublicKey;

    @PostMapping("/charge")
    public ResponseEntity charge(@Valid @RequestBody ChargeRequest chargeRequest, BindingResult result)
            throws StripeException {
        if(result.hasErrors()){
            return new ResponseEntity(result,HttpStatus.CONFLICT);
        }
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        int getTotalAmountOfItemsFoundInCard = cardRepository.getTotalPrice(getAuthenticatedUser.getId());
        chargeRequest.setDescription("Example charge");
        chargeRequest.setAmount(getTotalAmountOfItemsFoundInCard);
//        chargeRequest.setStripeToken(stripePublicKey);
        Charge charge = paymentsService.charge(chargeRequest);
        return new ResponseEntity(charge, HttpStatus.ACCEPTED);
    }
}
