package ru.woh.api.controllers.site;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.Role;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.UserRepository;
import ru.woh.api.requests.AvatarChangeRequest;
import ru.woh.api.requests.ChangePasswordRequest;
import ru.woh.api.requests.LoginRequest;
import ru.woh.api.requests.RegistrationRequest;
import ru.woh.api.services.GridFsService;
import ru.woh.api.services.ImageStorageService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.site.UserExtView;
import ru.woh.api.views.site.UserView;

import javax.annotation.security.RolesAllowed;
import java.awt.image.BufferedImage;
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
    private final ImageStorageService imageStorageService;

    @Autowired
    public UserController(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository,
        UserService userService,
        GridFsService gridFsService,
        ImageStorageService imageStorageService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
        this.gridFsService = gridFsService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping({"/user", "/user/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public UserView status() {
        return this.userService.getCurrenttUser().view();
    }

    @GetMapping({"/user/{id:[0-9]*}", "/user/{id:[0-9]*}/"})
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public UserView byId(@PathVariable("id") Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format(
            "User #%d not found",
            id
        ))).view();
    }

    @PostMapping({"/user/login", "/user/login/"})
    @RolesAllowed({ Role.ROLE_ANONYMOUS })
    public UserExtView login(@RequestBody LoginRequest loginRequest) {
        User user = this.userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        return new UserExtView(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getAvatar(),
            user.getRoleName(),
            user.getAnnotation(),
            user.getCreatedAt(),
            user.getToken()
        );
    }

    @PostMapping({"/user/register", "/user/register/"})
    @RolesAllowed({ Role.ROLE_ANONYMOUS })
    public ResponseEntity<UserView> register(@RequestBody RegistrationRequest registrationRequest) {
        User user = this.userService.getCurrenttUser();
        if (user != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "you are already authorized");
        }

        try {
            user = this.userService.authenticate(registrationRequest.getEmail(), registrationRequest.getPassword());
            if (user != null) {
                throw new HttpClientErrorException(HttpStatus.CONFLICT, "user already exists");
            }
        } catch (HttpClientErrorException e) { /* nop */ }

        user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(this.passwordEncoder.encode(registrationRequest.getPassword()));
        user.setName(registrationRequest.getName());
        user.setToken(this.passwordEncoder.encode(String.format(
            "%s%s%s",
            user.getEmail(),
            user.getPassword(),
            (new Date()).toString()
        )));
        user.setCreatedAt(new Date());

        user = this.userRepository.save(user);

        return ResponseEntity.created(URI.create("/user/")).body(user.view());
    }

    @PostMapping({"/user/save", "/user/save/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public UserView edit(@RequestBody UserView userView) {
        if (userView.getId() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "user id required");
        }

        User user = this.userRepository.findById(userView.getId())
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format("User #%d not found", userView.getId())));

        User currentUser = this.userService.getCurrenttUser();

        if (!currentUser.isModer() && !Objects.equals(currentUser.getId(), user.getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "You are not allowed to edit another user");
        }

        if (!Objects.equals(user.getName(), userView.getName())) {
            user.setName(userView.getName());
        }

        if (!Objects.equals(user.getAnnotation(), userView.getAnnotation())) {
            user.setAnnotation(userView.getAnnotation());
        }

        this.userRepository.save(user);

        return user.view();
    }

    @PostMapping({"/user/password", "/user/password/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ResponseEntity<Void> password(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        User currentUser = this.userService.getCurrenttUser();
        currentUser.setPassword(this.passwordEncoder.encode(changePasswordRequest.getPassword()));

        this.userRepository.save(currentUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping({"/user/avatar", "/user/avatar/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ResponseEntity<String> avatar(@RequestBody AvatarChangeRequest avatarChangeRequest) {
        if (!avatarChangeRequest.isValid()) {
            return ResponseEntity.badRequest().build();
        }

        BufferedImage avatar = avatarChangeRequest.getBufferedImage();
        if (avatar == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        User currentUser = this.userService.getCurrenttUser();

        HashMap<String, String> meta = new HashMap<>();
        meta.put("userId", currentUser.getId().toString());
        meta.put(HttpHeaders.CONTENT_TYPE, "image/jpeg");

        this.gridFsService.findByKeyValue("userId", currentUser.getId().toString())
            .forEach((Consumer<? super GridFSFile>) this.gridFsService::delete);

        String avatarId = this.imageStorageService.storeBufferedImage(
            avatar,
            String.format("user%d-avatar", currentUser.getId()),
            meta
        );
        if (avatarId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        currentUser.setAvatar(String.format("/image/%s", avatarId));
        this.userRepository.save(currentUser);

        var avatarUrl = String.format("/image/%s", avatarId);
        return ResponseEntity
            .created(URI.create(avatarUrl))
            .body(avatarUrl);
    }

    @PostMapping({"/user/avatar/drop", "/user/avatar/drop/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ResponseEntity<Void> dropAvatar() {
        User currentUser = this.userService.getCurrenttUser();
        if (currentUser.getAvatar() == null) {
            return ResponseEntity.ok().build();
        }

        this.imageStorageService.dropImage(currentUser.getAvatar());

        currentUser.setAvatar(null);
        this.userRepository.save(currentUser);

        return ResponseEntity.ok().build();
    }

}
