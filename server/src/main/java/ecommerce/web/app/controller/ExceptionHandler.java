package ecommerce.web.app.controller;

import ecommerce.web.app.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UsernameAlreadyExists ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UserNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(),ex));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PostCustomException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(PostCustomException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,ex.getMessage(),ex));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(EmailAlreadyExists ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PhoneNumberAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(PhoneNumberAlreadyExists ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FavoritesCustomException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(FavoritesCustomException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CardCustomException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(CardCustomException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage(),ex));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity(apiError, apiError.getStatus());
    }

}
