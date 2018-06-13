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
import org.springframework.web.servlet.view.RedirectView;
import ru.woh.api.ForbiddenException;
import ru.woh.api.UserAlreadyExists;
import ru.woh.api.models.RoleModel;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;
import ru.woh.api.views.UserView;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController extends BaseRestController {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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
    @RolesAllowed({RoleModel.USER, RoleModel.MODER, RoleModel.ADMIN})
    public UserView status(HttpServletRequest request) {
        this.needAuth(request);

        return this.getUser(request).view();
    }

    @PostMapping("/user/login")
    @RolesAllowed({RoleModel.ANONYMOUS})
    public UserExtView login(@RequestBody LoginData loginData) {
        UserModel user = this.userService.authenticate(loginData.getEmail(), loginData.getPassword());
        if (user == null) {
            throw new ForbiddenException();
        }

        return new UserExtView(user.getId(), user.getEmail(), user.getName(), user.getAvatar(), user.getToken());
    }

    @PostMapping("/user/register")
    @RolesAllowed({RoleModel.ANONYMOUS})
    public ResponseEntity<UserView> register(
        @RequestBody RegistrationData registrationData,
        HttpServletResponse response,
        HttpServletRequest request
    ) {
        UserModel user = this.userService.getUser(request);
        if (user != null) {
            response.addHeader("Location", "/");

            return new ResponseEntity<>(HttpStatus.TEMPORARY_REDIRECT);
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
    @RolesAllowed({RoleModel.USER, RoleModel.MODER, RoleModel.ADMIN})
    public RedirectView logout(HttpServletRequest request) {
        this.needAuth(request);
        this.userService.logout(request);

        return new RedirectView("/");
    }
}
