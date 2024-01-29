package ecommerce.web.app.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

   private HttpStatus status;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
   private LocalDateTime timestamp;
   private String message;
   private String debugMessage;

   private ApiError() {
       timestamp = LocalDateTime.now();
   }

   public ApiError(HttpStatus status, String message) {
       this();
       this.status = status;
       this.message = message;
       this.debugMessage = null;
   }
}