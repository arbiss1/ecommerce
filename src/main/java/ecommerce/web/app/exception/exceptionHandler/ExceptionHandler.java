package ecommerce.web.app.exception.exceptionHandler;

import ecommerce.web.app.exception.ApiError;
import ecommerce.web.app.exception.customExceptions.UsernameAlreadyExists;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler {
//
    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UsernameAlreadyExists ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity(apiError, apiError.getStatus());
    }

}
