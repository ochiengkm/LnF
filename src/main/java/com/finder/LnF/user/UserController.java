package com.finder.LnF.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class UserController {
    private final UserService userService;

}
