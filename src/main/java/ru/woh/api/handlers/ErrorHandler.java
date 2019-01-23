package ru.woh.api.handlers;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.views.site.ErrorView;

import java.util.Date;

@ControllerAdvice
public class ErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorView> exception(HttpClientErrorException e) {
        return getErrorViewResponseEntity(
            e.getRawStatusCode(),
            e.getStatusCode().getReasonPhrase(),
            e
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorView> exception(Exception e) {
        return getErrorViewResponseEntity(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            e
        );
    }

    private ResponseEntity<ErrorView> getErrorViewResponseEntity(
        int statusCode,
        String errorMessage,
        @NotNull Throwable e
    ) {
        e.printStackTrace();
        this.logger.error(e.getMessage(), e);

        var error = new ErrorView();
        error.setMessage(e.getMessage());
        error.setTimestamp(new Date());
        error.setStatus(statusCode);
        error.setError(errorMessage);

        return ResponseEntity.status(statusCode).body(error);
    }
}
