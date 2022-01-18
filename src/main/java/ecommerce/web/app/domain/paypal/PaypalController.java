package ecommerce.web.app.domain.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaypalController {

    @Autowired
    PaypalService service;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/pay")
    public ResponseEntity payment(@RequestBody PaypalOrder paypalOrder) throws PayPalRESTException {

            Payment payment = service.createPayment(paypalOrder.getPrice(), paypalOrder.getCurrency(), paypalOrder.getMethod(),
                    paypalOrder.getIntent(), paypalOrder.getDescription());
            service.executePayment(payment.getId(), payment.getPayer().getFundingOptionId());
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return new ResponseEntity(payment, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity("not approved", HttpStatus.CONFLICT);
            }
        }
    }

//    @GetMapping(value = CANCEL_URL)
//    public String cancelPay() {
//        return "cancelorder";
//    }
//
//    @GetMapping(value = SUCCESS_URL)
//    public ResponseEntity successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
//        try {
//            Payment payment = service.executePayment(paymentId, payerId);
//            System.out.println(payment.toJSON());
//            if (payment.getState().equals("approved")) {
//                return new ResponseEntity(payment, HttpStatus.ACCEPTED);
//            }
//        } catch (PayPalRESTException e) {
//            System.out.println(e.getMessage());
//        }
//        return new ResponseEntity("Not accepted",HttpStatus.NO_CONTENT);
//    }

