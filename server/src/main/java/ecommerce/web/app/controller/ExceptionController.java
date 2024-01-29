package ecommerce.web.app.controller;

import ecommerce.web.app.exceptions.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsernameAlreadyExists.class)
    public ResponseEntity<ApiError> handleUserAlreadyExists(UsernameAlreadyExists ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage()));
    }

    @ExceptionHandler(BindingException.class)
    public ResponseEntity<ApiError> handleBindingException(BindingException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleUnauthorized(AuthenticationException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED,ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,ex.getMessage()));
    }

    @ExceptionHandler(PostCustomException.class)
    public ResponseEntity<ApiError> handlePostCustomException(PostCustomException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND,ex.getMessage()));
    }

    @ExceptionHandler(ImageCustomException.class)
    public ResponseEntity<ApiError> handleImageCustomException(ImageCustomException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage()));
    }

    @ExceptionHandler(FavoritesCustomException.class)
    public ResponseEntity<ApiError> handleFavoritesCustomException(FavoritesCustomException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT,ex.getMessage()));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
