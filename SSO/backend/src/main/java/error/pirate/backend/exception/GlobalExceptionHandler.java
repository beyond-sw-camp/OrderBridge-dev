package error.pirate.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("httpStatus", ex.getErrorCode().getHttpStatus());
        errorResponse.put("errorCode", ex.getErrorCode().getCode());
        errorResponse.put("message", ex.getErrorCode().getMessage());

        return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
    }
}