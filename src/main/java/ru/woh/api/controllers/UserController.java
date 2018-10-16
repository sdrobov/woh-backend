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
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static class LoginRequest {
        protected String email;
        protected String password;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class RegistrationRequest extends LoginRequest {
        protected String name = "";
    }

    @Getter
    @Setter
    public static class UserExtView extends UserView {
        protected String token;

        UserExtView(Long id, String email, String name, String avatar, String role, String annotation, String token) {
            super(id, email, name, avatar, role, annotation);
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

    @NoArgsConstructor
    @Getter
    @Setter
    public static class AvatarChangeRequest {
        protected MultipartFile file;
        protected Integer x1;
        protected Integer y1;
        protected Integer x2;
        protected Integer y2;
        protected Integer h;
        protected Integer w;

        Boolean isValid() {
            return !this.file.isEmpty()
                && this.x1 != null
                && this.y1 != null
                && this.x2 != null
                && this.y2 != null
                && this.h != null
                && this.w != null;
        }

        Integer getWidth() {
            return this.w;
        }

        Integer getHeight() {
            return this.h;
        }

        BufferedImage getBufferedImage() {
            try {
                BufferedImage originalAvatar = ImageIO.read(new ByteArrayInputStream(this.file.getBytes()));
                Integer newWidth = this.getWidth();
                Integer newHeight = this.getHeight();
                if (!Objects.equals(newWidth, newHeight)) {
                    newWidth = newHeight = Math.max(newWidth, newHeight);
                }

                BufferedImage crop1 = originalAvatar.getSubimage(this.x1, this.y1, newWidth, newHeight);
                BufferedImage crop2 = null;
                if (crop1.getWidth() > 1000 || crop1.getHeight() > 1000) {
                    crop2 = new BufferedImage(1000, 1000, crop1.getType());
                    Graphics2D graphics2D = crop2.createGraphics();

                    if (crop2.getType() == BufferedImage.TYPE_INT_ARGB) {
                        graphics2D.setComposite(AlphaComposite.Src);
                    }

                    graphics2D.drawImage(crop1, 0, 0, 1000, 1000, null);
                    graphics2D.dispose();
                }

                return crop2 != null ? crop2 : crop1;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
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
    public UserExtView login(@RequestBody LoginRequest loginRequest) {
        User user = this.userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        return new UserExtView(user.getId(),
            user.getEmail(),
            user.getName(),
            user.getAvatar(),
            user.getRoleName(),
            user.getAnnotation(),
            user.getToken());
    }

    @PostMapping("/user/register")
    @RolesAllowed({Role.ROLE_ANONYMOUS})
    public ResponseEntity<UserView> register(@RequestBody RegistrationRequest registrationRequest) {
        User user = this.userService.getCurrenttUser();
        if (user != null) {
            throw new BadRequestException("you are already authorized");
        }

        try {
            user = this.userService.authenticate(registrationRequest.getEmail(), registrationRequest.getPassword());
            if (user != null) {
                throw new UserAlreadyExistsException("user already exists");
            }
        } catch (NotFoundException e) { /* nop */ }

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
    public ResponseEntity<Void> avatar(@ModelAttribute AvatarChangeRequest avatarChangeRequest) {
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
        meta.put(HttpHeaders.CONTENT_TYPE, avatarChangeRequest.getFile().getContentType());

        this.gridFsService.findByKeyValue("userId", currentUser.getId().toString())
            .forEach((Consumer<? super GridFSFile>) this.gridFsService::delete);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(avatar, "jpeg", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        String avatarId = this.gridFsService.store(
            inputStream,
            avatarChangeRequest.getFile().getName(),
            avatarChangeRequest.getFile().getContentType(),
            meta
        );
        if (avatarId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        currentUser.setAvatar(avatarId);
        this.userRepository.save(currentUser);

        return ResponseEntity.created(URI.create("/user/avatar/" + currentUser.getId().toString())).build();
    }

    @PostMapping("/user/avatar/drop/")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity<Void> dropAvatar() {
        User currentUser = this.userService.getCurrenttUser();
        ResponseEntity currentAvatar = this.getAvatar(currentUser);

        if (currentAvatar.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.ok().build();
        }

        GridFSFile avatar = this.gridFsService.findById(currentUser.getAvatar());
        this.gridFsService.delete(avatar);
        currentUser.setAvatar(null);

        this.userRepository.save(currentUser);

        return ResponseEntity.ok().build();
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
