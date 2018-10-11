package ru.woh.api.controllers;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.woh.api.exceptions.BadRequestException;
import ru.woh.api.exceptions.ForbiddenException;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.exceptions.UserAlreadyExistsException;
import ru.woh.api.models.Role;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.UserRepository;
import ru.woh.api.services.GridFsService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.UserView;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

@RestController
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GridFsService gridFsService;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          UserService userService,
                          GridFsService gridFsService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
        this.gridFsService = gridFsService;
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
        protected String name = "";
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UserExtView extends UserView {
        protected String token;

        UserExtView(Long id, String email, String name, String avatar, String role, String token) {
            super(id, email, name, avatar, role);
            this.token = token;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ChangePasswordRequest {
        protected String password;
        protected String password2;

        Boolean isValid() {
            return Objects.equals(this.password, this.password2)
                    && this.password != null
                    && !this.password.isEmpty();
        }
    }

    @GetMapping("/user")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public UserView status() {
        return this.userService.getCurrenttUser().view();
    }

    @GetMapping("/user/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public UserView byId(@PathVariable("id") Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "User #%d not found",
            id))).view();
    }

    @PostMapping("/user/login")
    @RolesAllowed({Role.ROLE_ANONYMOUS})
    public UserExtView login(@RequestBody LoginData loginData) {
        User user = this.userService.authenticate(loginData.getEmail(), loginData.getPassword());

        return new UserExtView(user.getId(),
            user.getEmail(),
            user.getName(),
            user.getAvatar(),
            user.getRoleName(),
            user.getToken());
    }

    @PostMapping("/user/register")
    @RolesAllowed({Role.ROLE_ANONYMOUS})
    public ResponseEntity<UserView> register(@RequestBody RegistrationData registrationData) {
        User user = this.userService.getCurrenttUser();
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
        user.setCreatedAt(new Date());
        user.setAvatar("/static/nopic.gif");

        user = this.userRepository.save(user);

        return ResponseEntity.created(URI.create("/user/")).body(user.view());
    }

    @PostMapping("/user/save")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public UserView save(@RequestBody UserView userView) {
        User user = this.userRepository.findById(userView.getId())
            .orElseThrow(() -> new NotFoundException(String.format("User #%d not found", userView.getId())));

        User currentUser = this.userService.getCurrenttUser();

        if (!currentUser.isModer() && !Objects.equals(currentUser.getId(), user.getId())) {
            throw new ForbiddenException("You are not allowed to edit another user");
        }

        if (!Objects.equals(user.getName(), userView.getName())) {
            user.setName(userView.getName());
        }

        this.userRepository.save(user);

        return user.view();
    }

    @PostMapping("/user/password")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity<Void> password(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        User currentUser = this.userService.getCurrenttUser();
        currentUser.setPassword(this.passwordEncoder.encode(changePasswordRequest.getPassword()));

        this.userRepository.save(currentUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/avatar/")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity<byte[]> avatar() {
        return this.getAvatar(this.userService.getCurrenttUser());
    }

    @GetMapping("/user/avatar/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity<byte[]> avatar(@PathVariable("id") Long id) {
        User user;
        if (id == null) {
            user = this.userService.getCurrenttUser();
        } else {
            user = this.userRepository.findById(id).orElse(null);
        }

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return this.getAvatar(user);
    }

    @PostMapping("/user/avatar/")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity<Void> avatar(@RequestParam("file") MultipartFile file) throws IOException {
        User currentUser = this.userService.getCurrenttUser();

        HashMap<String, String> meta = new HashMap<>();
        meta.put("userId", currentUser.getId().toString());
        meta.put(HttpHeaders.CONTENT_TYPE, file.getContentType());

        this.gridFsService.findByKeyValue("userId", currentUser.getId().toString())
            .forEach((Consumer<? super GridFSFile>) this.gridFsService::delete);

        String avatarId = this.gridFsService.store(file.getInputStream(), file.getName(), file.getContentType(), meta);
        if (avatarId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        currentUser.setAvatar(avatarId);
        this.userRepository.save(currentUser);

        return ResponseEntity.created(URI.create("/user/avatar/" + currentUser.getId().toString())).build();
    }

    private ResponseEntity<byte[]> getAvatar(User user) {
        if (user.getAvatar() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        GridFSFile gridfsFile = this.gridFsService.findById(user.getAvatar());
        if (gridfsFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, (String) gridfsFile.getMetadata().get(HttpHeaders.CONTENT_TYPE))
            .body(this.gridFsService.getFile(gridfsFile));
    }
}
