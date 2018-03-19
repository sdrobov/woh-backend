package ru.woh.api.controllers;

import lombok.NoArgsConstructor;
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

    @NoArgsConstructor
    public static class LoginData {
        protected String email;
        protected String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @NoArgsConstructor
    public static class RegistrationData extends LoginData {
        protected String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @GetMapping("/user")
    public UserView status(HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null) {
            throw new ForbiddenException();
        }

        return user.view();
    }

    @PostMapping("/user/login")
    public UserView login(@RequestBody LoginData loginData, HttpSession session) {
        UserModel user = this.userService.authenticate(loginData.getEmail(), loginData.getPassword());
        if (user == null) {
            throw new ForbiddenException();
        }
        this.userService.setUser(user, session);

        return user.view();
    }

    @PostMapping("/user/register")
    public ResponseEntity<UserView> register(@RequestBody RegistrationData registrationData, HttpSession session, HttpServletResponse response) {
        UserModel user = this.userService.getUser(session);
        if (user != null) {
            response.addHeader("Location", "/");

            return new ResponseEntity<>(HttpStatus.MOVED_TEMPORARILY);
        }

        user = this.userService.authenticate(registrationData.getEmail(), registrationData.getPassword());
        if (user != null) {
            throw new UserAlreadyExists("user already exists");
        }

        user = new UserModel();
        user.setEmail(registrationData.getEmail());
        user.setPassword(this.passwordEncoder.encode(registrationData.getPassword()));
        user.setName(registrationData.getName());

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
