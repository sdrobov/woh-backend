package ru.woh.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.woh.api.models.UserModel;
import ru.woh.api.models.UserRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel user = this.userRepository.findFirstByEmail(s).orElseThrow(NotFoundException::new);

        return new User(user.getName(), user.getPassword(), Collections.emptyList());
    }
}
