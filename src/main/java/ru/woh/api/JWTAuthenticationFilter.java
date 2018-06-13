package ru.woh.api;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.woh.api.models.UserModel;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    JWTAuthenticationFilter(
        UserService userService, AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {
        UserModel user = this.userService.getUser(request);
        if (user == null) {
            return this.authenticationManager.authenticate(UserModel.anonymousAuthenticationToken());
        }

        return this.authenticationManager.authenticate(user.usernamePasswordAuthenticationToken());
    }

    @Override protected void successfulAuthentication(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult
    ) {
        UserModel user = (UserModel) authResult.getPrincipal();
        response.addHeader("Authorization", String.format("Bearer %s", user.getToken()));
    }
}
