package me.erickmrtz.demo.firebase.exception;

import me.erickmrtz.demo.firebase.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<?> handleNotFoundException(NotFoundException exception) {
        Response response = new Response(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(FirebaseException.class)
    protected ResponseEntity<?> handleValidationException(FirebaseException exception) {
        Response response = new Response(HttpStatus.BAD_GATEWAY, exception.getMessage());
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<?> handleBadRequestException(BadRequestException exception) {
        Response response = new Response(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
