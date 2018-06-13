package ru.woh.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;
import ru.woh.api.models.UserToken;
import ru.woh.api.models.UserTokenRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userTokenRepository = userTokenRepository;
    }

    public UserModel getUser(HttpServletRequest request)
    {
        String token = this.getToken(request);
        if (token == null) {
            return null;
        }

        return this.userRepository.findFirstByToken(token).orElse(null);
    }

    public UserModel authenticate(String login, String password) {
        UserModel user = this.userRepository.findFirstByEmail(login).orElse(null);
        if (user == null) {
            return null;
        }

        if (this.passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public void logout(HttpServletRequest request) {
        String token = this.getToken(request);
        if (token == null) {
            return;
        }

        UserToken userToken = this.userTokenRepository.findByToken(token);
        if (userToken == null) {
            return;
        }

        this.userTokenRepository.delete(userToken);
    }

    private String getToken(HttpServletRequest request)
    {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return  null;
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
