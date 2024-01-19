package ecommerce.web.app.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import ecommerce.web.app.entities.ChargeRequest;
import ecommerce.web.app.entities.User;
import ecommerce.web.app.exceptions.ChargeCustomException;
import ecommerce.web.app.exceptions.UserNotFoundException;
import ecommerce.web.app.repository.CardRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class StripeService {

    private final CardRepository cardRepository;
    private final UserService userService;
    private final MessageSource messageByLocale;
    private final Locale locale = Locale.ENGLISH;
    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;
    public StripeService(CardRepository cardRepository, UserService userService, MessageSource messageByLocale){
        this.cardRepository = cardRepository;
        this.userService = userService;
        this.messageByLocale = messageByLocale;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    public Charge charge(ChargeRequest chargeRequest, BindingResult result) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException, UserNotFoundException, ChargeCustomException {
        if(result.hasErrors()){
            throw new ChargeCustomException(
                    messageByLocale.getMessage(result.getAllErrors().toString(), null, locale)
            );
        }
        User getAuthenticatedUser = userService.getAuthenticatedUser();

        int getTotalAmountOfItemsFoundInCard = cardRepository.getTotalPrice(getAuthenticatedUser.getId());
        chargeRequest.setDescription("Example charge");
        chargeRequest.setAmount(getTotalAmountOfItemsFoundInCard);
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount() * 100);
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", "tok_mastercard");
        return Charge.create(chargeParams);
    }
}
