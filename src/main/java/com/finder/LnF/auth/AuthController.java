package com.finder.LnF.auth;

import com.finder.LnF.user.User;
import com.finder.LnF.user.UserService;
import com.finder.LnF.utils.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/signup")
    public User registerUser(@RequestBody AuthRequest authRequest) {
        return userService.registerUser(authRequest);
    }
}
