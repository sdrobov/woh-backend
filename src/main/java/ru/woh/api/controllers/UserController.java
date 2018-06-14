package ru.woh.api.controllers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.exceptions.BadRequestException;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.exceptions.UserAlreadyExistsException;
import ru.woh.api.services.UserService;
import ru.woh.api.models.Role;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.UserRepository;
import ru.woh.api.views.UserView;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class LoginData {
        protected String email;
        protected String password;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class RegistrationData extends LoginData {
        protected String name;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserExtView extends UserView {
        protected String token;

        UserExtView(Long id, String email, String name, String avatar, String token) {
            super(id, email, name, avatar);
            this.token = token;
        }
    }

    @GetMapping("/user")
    @RolesAllowed({Role.USER, Role.MODER, Role.ADMIN})
    public UserView status() {
        return this.userService.geCurrenttUser().view();
    }

    @PostMapping("/user/login")
    @RolesAllowed({Role.ANONYMOUS})
    public UserExtView login(@RequestBody LoginData loginData) {
        User user = this.userService.authenticate(loginData.getEmail(), loginData.getPassword());

        return new UserExtView(user.getId(), user.getEmail(), user.getName(), user.getAvatar(), user.getToken());
    }

    @PostMapping("/user/register")
    @RolesAllowed({Role.ANONYMOUS})
    public ResponseEntity<UserView> register(@RequestBody RegistrationData registrationData) {
        User user = this.userService.geCurrenttUser();
        if (user != null) {
            throw new BadRequestException("you are already authorized");
        }

        try {
            user = this.userService.authenticate(registrationData.getEmail(), registrationData.getPassword());
            if (user != null) {
                throw new UserAlreadyExistsException("user already exists");
            }
        } catch (NotFoundException e) { /* nop */ }

        user = new User();
        user.setEmail(registrationData.getEmail());
        user.setPassword(this.passwordEncoder.encode(registrationData.getPassword()));
        user.setName(registrationData.getName());
        user.setToken(this.passwordEncoder.encode(String.format(
            "%s%s%s",
            user.getEmail(),
            user.getPassword(),
            (new Date()).toString()
        )));

        user = this.userRepository.save(user);

        return new ResponseEntity<>(user.view(), HttpStatus.CREATED);
    }
}
