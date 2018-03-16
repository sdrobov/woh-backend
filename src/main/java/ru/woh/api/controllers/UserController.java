package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.woh.api.ForbiddenException;
import ru.woh.api.UserAlreadyExists;
import ru.woh.api.UserService;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;
import ru.woh.api.views.UserView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected UserRepository userRepository;

    @GetMapping("/user")
    public UserView status(HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null) {
            throw new ForbiddenException();
        }

        return user.view();
    }

    @PostMapping("/user/login")
    public UserView login(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            HttpSession session
    ) {
        UserModel user = this.userService.authenticate(email, password);
        if (user == null) {
            throw new ForbiddenException();
        }
        this.userService.setUser(user, session);

        return user.view();
    }

    @PostMapping("/user/register")
    public ResponseEntity<UserView> register(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            HttpSession session,
            HttpServletResponse response
    ) {
        UserModel user = this.userService.getUser(session);
        if (user != null) {
            response.addHeader("Location", "/");

            return new ResponseEntity<>(HttpStatus.MOVED_TEMPORARILY);
        }

        user = this.userService.authenticate(email, password);
        if (user != null) {
            throw new UserAlreadyExists("user already exists");
        }

        user = new UserModel();
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setName(name);

        user = this.userRepository.save(user);

        return new ResponseEntity<>(user.view(), HttpStatus.CREATED);
    }

    @GetMapping("/user/logout")
    public RedirectView logout(HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null) {
            throw new ForbiddenException();
        }

        this.userService.logout(session);

        return new RedirectView("/");
    }
}
