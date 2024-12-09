package com.finder.LnF.user;

import com.finder.LnF.token.JwtUtil;
import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String MESSAGE = "Success!";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
       User user = userRepository.findByUsername(username).orElseThrow(
               ()->new UsernameNotFoundException("user not found"));

       return new org.springframework.security.core.userdetails.User(
               user.getUsername(),
               user.getPassword(),
               user.getRoles()
                       .stream()
                       .map(SimpleGrantedAuthority::new)
                       .collect(Collectors.toList())
       );
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e){
            return ResponseEntity.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .message("Incorrect username or password")
                    .build();
        }

        String token = jwtUtil.generateToken(loginRequest.getUsername());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return ResponseEntity.builder()
                .statusCode(HttpStatus.OK.value())
                .entity(token)
                .message(MESSAGE)
                .build();
    }
}
