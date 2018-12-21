package ru.woh.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.UserRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.findFirstByEmail(s).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
            String.format("User %s not found", s)
        ));

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), Collections.emptyList());
    }
}
