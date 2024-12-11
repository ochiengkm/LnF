package com.finder.LnF.auth;

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

    @PostMapping("/user/signup")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        return userService.registerUser(authRequest);
    }

    @DeleteMapping("/user/self-delete")
    public ResponseEntity<?> deleteUser(@RequestParam String username){
        return userService.deleteUser(username);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest authRequest) {
        return userService.registerAdmin(authRequest);
    }

    @DeleteMapping("/admin/delete-user")
    public ResponseEntity<?> adminDeleteUser(@RequestParam String username) {
        return userService.adminDeleteUser(username);
    }
}
