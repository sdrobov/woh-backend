package ru.woh.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;

import javax.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserModel getUser(HttpSession session) {
        return (UserModel)session.getAttribute("user");
    }

    public UserModel setUser(UserModel user, HttpSession session) {
        session.setAttribute("user", user);

        return this.getUser(session);
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
}
