package ru.woh.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists() {
    }

    public UserAlreadyExists(String s) {
        super(s);
    }

    public UserAlreadyExists(String s, Throwable throwable) {
        super(s, throwable);
    }

    public UserAlreadyExists(Throwable throwable) {
        super(throwable);
    }

    public UserAlreadyExists(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
