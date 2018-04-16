package ru.woh.api.controllers;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.woh.api.ForbiddenException;
import ru.woh.api.UserAlreadyExists;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;
import ru.woh.api.views.UserView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserController extends BaseRestController {
    protected final PasswordEncoder passwordEncoder;

    protected final UserRepository userRepository;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

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

    @NoArgsConstructor
    public static class UserExtView extends UserView {
        protected String token;

        public UserExtView(Long id, String email, String name, String avatar) {
            super(id, email, name, avatar);
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    @GetMapping("/user")
    public UserView status(HttpSession session) {
        this.needAuth(session);

        return this.getUser(session).view();
    }

    @PostMapping("/user/login")
    public UserExtView login(@RequestBody LoginData loginData, HttpSession session) {
        UserModel user = this.userService.authenticate(loginData.getEmail(), loginData.getPassword());
        if (user == null) {
            throw new ForbiddenException();
        }

        String token = this.userService.setUser(user);
        if (token == null) {
            throw new ForbiddenException("cant save token");
        }

        UserExtView view = new UserExtView(user.getId(), user.getEmail(), user.getName(), user.getAvatar());
        view.token = token;

        return view;
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
        this.needAuth(session);
        this.userService.logout(session);

        return new RedirectView("/");
    }
}
