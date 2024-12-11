package com.finder.LnF.user;

import com.finder.LnF.auth.AuthRequest;
import com.finder.LnF.utils.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    public ResponseEntity<?> registerUser(AuthRequest authRequest) {

        String username = authRequest.getUsername();
        if (userRepository.findByUsername(username).isEmpty()) {

            User user = new User();
            user.setUsername(authRequest.getUsername());
            user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
            user.setUserType("USER");
            userRepository.save(user);

            return ResponseEntity.builder()
                    .message("Registered Successfully")
                    .statusCode(HttpStatus.CREATED.value())
                    .entity(user).build();
        } else {
            return ResponseEntity.builder()
                    .message("Username already exists, choose another one")
                    .statusCode(HttpStatus.BAD_GATEWAY.value())
                    .entity(null).build();
        }
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<?> deleteUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new NoSuchElementException("User not found"));
        if (user.getUserType().equals("USER") && user.getUsername().equals(principal)) {

            userRepository.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public User registerAdmin(AuthRequest authRequest) {
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setUserType("ADMIN");
        return userRepository.save(user);
    }

    public ResponseEntity<?> adminDeleteUser(String username) {
        ResponseEntity<?> response = new ResponseEntity<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();

        User user = userRepository.findByUsername(username).orElseThrow(
                ()-> new NoSuchElementException("User not found"));

        if (!user.getUserType().equals("ADMIN") && !user.getUsername().equals(principal)) {
            userRepository.delete(user);
            response.setMessage("User has been deleted");
        }else {
            response.setMessage("User cannot be deleted");
        }
        return response;
    }
}
