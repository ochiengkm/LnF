package com.finder.LnF.user;

import com.finder.LnF.auth.AuthRequest;
import com.finder.LnF.utils.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(AuthRequest authRequest) {
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        return userRepository.save(user);
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<?> deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new NoSuchElementException("User not found"));

        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
