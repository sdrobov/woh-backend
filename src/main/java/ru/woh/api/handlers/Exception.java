package ru.woh.api.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.views.site.ErrorView;

import java.util.Date;

@ControllerAdvice
public class Exception {
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorView> exception(HttpClientErrorException e) {
        e.printStackTrace();

        var error = new ErrorView();
        error.setMessage(e.getMessage());
        error.setTimestamp(new Date());
        error.setStatus(e.getRawStatusCode());
        error.setError(e.getStatusCode().getReasonPhrase());

        return ResponseEntity.status(e.getStatusCode()).body(error);
    }

    @ExceptionHandler(java.lang.Exception.class)
    public ResponseEntity<ErrorView> exception(java.lang.Exception e) {
        e.printStackTrace();

        var error = new ErrorView();
        error.setMessage(e.getMessage());
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
