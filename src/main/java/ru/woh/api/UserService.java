package ru.woh.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;
import ru.woh.api.models.UserToken;
import ru.woh.api.models.UserTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

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

    public UserModel getUser(HttpSession session) {
        Integer userId = (Integer)session.getAttribute("user");
        if (userId != null && userId > 0) {
            return this.userRepository.findOne(Long.valueOf(userId));
        }

        return null;
    }

    public UserModel getUser(HttpServletRequest request)
    {
        String token = this.getToken(request);
        if (token == null) {
            return null;
        }

        UserToken userToken = this.userTokenRepository.findByToken(token);
        if (userToken == null) {
            return null;
        }

        if (userToken.expiresAt.before(Date.from(Instant.now()))) {
            this.userTokenRepository.delete(userToken);

            return null;
        }

        return this.userRepository.findOne(userToken.userId);
    }

    public UserModel setUser(UserModel user, HttpSession session) {
        session.setAttribute("user", user.getId());

        return user;
    }

    public String setUser(UserModel user) {
        UserToken userToken = new UserToken();
        userToken.token = UUID.randomUUID().toString();
        userToken.userId = user.getId();
        userToken.expiresAt = Date.from(Instant.now().plusSeconds(3600 * 24 * 30));

        this.userTokenRepository.save(userToken);

        return userToken.token;
    }

    public UserModel authenticate(String login, String password) {
        UserModel user = this.userRepository.findFirstByEmail(login);
        if (user == null) {
            return null;
        }

        if (this.passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    public void logout(HttpSession session) {
        session.removeAttribute("user");
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

    protected String getToken(HttpServletRequest request)
    {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            return  null;
        }

        if (!authorization.toLowerCase().contains("bearer")) {
            return null;
        }

        return authorization.split(" ")[1];
    }
}
