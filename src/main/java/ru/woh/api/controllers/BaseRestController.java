package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import ru.woh.api.ForbiddenException;
import ru.woh.api.UserService;
import ru.woh.api.models.UserModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

abstract public class BaseRestController {
    @Autowired
    protected UserService userService;

    public void needAuth(HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null) {
            throw new ForbiddenException();
        }
    }

    public void needAuth(HttpServletRequest request) {
        UserModel user = this.userService.getUser(request);
        if (user == null) {
            throw new ForbiddenException();
        }
    }

    public UserModel getUser(HttpSession session) {
        return this.userService.getUser(session);
    }

    public UserModel getUser(HttpServletRequest request) {
        return this.userService.getUser(request);
    }

    public void needModer(HttpSession session) {
        this.needAuth(session);
        if (!this.getUser(session).isModer()) {
            throw new ForbiddenException();
        }
    }

    public void needModer(HttpServletRequest request) {
        this.needAuth(request);
        if (!this.getUser(request).isModer()) {
            throw new ForbiddenException();
        }
    }

    public void needAdmin(HttpSession session) {
        this.needAuth(session);
        if (!this.getUser(session).isAdmin()) {
            throw new ForbiddenException();
        }
    }

    public void needAdmin(HttpServletRequest request) {
        this.needAuth(request);
        if (!this.getUser(request).isAdmin()) {
            throw new ForbiddenException();
        }
    }
}
