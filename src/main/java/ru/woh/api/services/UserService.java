package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserByAuthToken(HttpServletRequest request) {
        String token = this.getToken(request);
        if (token == null) {
            return null;
        }

        return this.userRepository.findFirstByToken(token).orElse(null);
    }

    public User getCurrenttUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return auth instanceof UsernamePasswordAuthenticationToken ? (User) auth.getPrincipal() : null;
    }

    public User authenticate(String login, String password) {
        User user = this.userRepository.findFirstByEmail(login).orElseThrow(() -> new HttpClientErrorException(
            HttpStatus.NOT_FOUND, "user not found"));

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "user not found");
        }

        return user;
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return null;
        }

        if (!authorization.toLowerCase().contains("bearer")) {
            return null;
        }

        if (authorization.split(" ").length != 2) {
            return null;
        }

        return authorization.split(" ")[1];
    }
}
