package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.repositories.UserRepository;

@RestController
public class AdminUserController {
    private final UserRepository userRepository;

    @Autowired
    public AdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/{id:[0-9]*}/delete")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        var user = this.userRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format(
            "user #%d not found",
            id
        )));

        this.userRepository.delete(user);

        return ResponseEntity.ok().build();
    }
}
