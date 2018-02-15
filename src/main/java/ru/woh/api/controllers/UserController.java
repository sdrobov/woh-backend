package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.ForbiddenException;
import ru.woh.api.UserService;
import ru.woh.api.models.UserModel;
import ru.woh.api.views.UserView;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public UserView status(HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null) {
            throw new ForbiddenException();
        }

        return user.view();
    }

    @PostMapping("/user/login")
    public UserView login(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password, HttpSession session) {
        UserModel user = this.userService.authenticate(login, password);
        if (user == null) {
            throw new ForbiddenException();
        }
        this.userService.setUser(user, session);

        return user.view();
    }
}
