package dev.khanh.learnspring.exception;

import dev.khanh.learnspring.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> runtimeExceptionHandler(Exception e) {
        log.error(e.getMessage(), e);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<Void>> appExceptionHandler(AppException e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(e.getErrorCode().getCode());
        apiResponse.setMessage(e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = new HashMap<>();
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            ConstraintViolation<?> constraintViolation = e.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();


        } catch (IllegalArgumentException ignored) {

        }

        ApiResponse<Void> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(attributes != null ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage());

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse<Void>> authorizationDeniedExceptionHandler(AuthorizationDeniedException ignoredE) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(
                        ApiResponse.<Void>builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
