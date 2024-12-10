package com.finder.LnF.auth;

import com.finder.LnF.token.JwtUtil;
import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            String jwtToken = jwtUtil.generateToken(authRequest.getUsername());

            return ResponseEntity.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Success!")
                    .entity(new AuthResponse(jwtToken))
                    .build();
        }
        return ResponseEntity.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid Credentials")
                .build();
    }
}
