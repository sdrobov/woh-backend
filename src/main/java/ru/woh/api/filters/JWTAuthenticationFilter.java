package ru.woh.api.filters;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.woh.api.services.UserService;
import ru.woh.api.models.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(
        UserService userService, AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        User user = this.userService.findUserByAuthToken(request);
        if (user == null) {
            return this.authenticationManager.authenticate(User.anonymousAuthenticationToken());
        }

        return this.authenticationManager.authenticate(user.usernamePasswordAuthenticationToken());
    }

    @Override protected void successfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult
    ) {
        User user = (User) authResult.getPrincipal();
        response.addHeader("Authorization", String.format("Bearer %s", user.getToken()));
    }
}
