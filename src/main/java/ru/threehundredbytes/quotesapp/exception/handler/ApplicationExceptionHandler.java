package ru.threehundredbytes.quotesapp.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.threehundredbytes.quotesapp.api.model.response.ErrorResponseDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var responseDTO = ErrorResponseDTO.builder()
                .message("Invalid request")
                .status(BAD_REQUEST.value())
                .error("Validation error")
                .path(request.getRequestURI())
                .build();

        exception.getBindingResult().getFieldErrors()
                .forEach(e -> responseDTO.getErrors().put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(responseDTO);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        var responseDTO = ErrorResponseDTO.builder()
                .message("Invalid request")
                .status(BAD_REQUEST.value())
                .error("Validation error")
                .path(request.getRequestURI())
                .build();

        exception.getConstraintViolations()
                .forEach(v -> responseDTO.getErrors().put(v.getPropertyPath().toString(), v.getMessage()));

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(responseDTO);
    }
}
