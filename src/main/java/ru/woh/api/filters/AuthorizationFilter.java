package ru.woh.api.filters;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import ru.woh.api.models.User;
import ru.woh.api.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final UserService userService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        Authentication auth = this.getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        User user = this.userService.findUserByAuthToken(request);
        if (user == null) {
            return User.anonymousAuthenticationToken();
        }

        return user.usernamePasswordAuthenticationToken();
    }
}
