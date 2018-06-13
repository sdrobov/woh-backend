package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.woh.api.ForbiddenException;
import ru.woh.api.UserService;
import ru.woh.api.models.UserModel;

import javax.servlet.http.HttpServletRequest;

abstract public class BaseRestController {
    @Autowired
    protected UserService userService;

    UserModel getUser(HttpServletRequest request) {
        return (UserModel) ((UsernamePasswordAuthenticationToken) request.getUserPrincipal()).getPrincipal();
    }

    void needAuth(HttpServletRequest request) {
        UserModel user = this.userService.getUser(request);
        if (user == null) {
            throw new ForbiddenException();
        }
    }

    void needModer(HttpServletRequest request) {
        this.needAuth(request);
        if (!this.getUser(request).isModer()) {
            throw new ForbiddenException();
        }
    }

    void needAdmin(HttpServletRequest request) {
        this.needAuth(request);
        if (!this.getUser(request).isAdmin()) {
            throw new ForbiddenException();
        }
    }
}
