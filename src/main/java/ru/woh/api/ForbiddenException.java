package ru.woh.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
    }

    public ForbiddenException(String s) {
        super(s);
    }

    public ForbiddenException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ForbiddenException(Throwable throwable) {
        super(throwable);
    }

    public ForbiddenException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
