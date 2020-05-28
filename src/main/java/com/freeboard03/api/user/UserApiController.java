package com.freeboard03.api.user;

import com.freeboard03.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final HttpSession httpSession;
    private final UserService userService;

    @PostMapping
    private void join(@RequestBody UserForm user){
        userService.join(user);
    }

    @PostMapping(params = {"type=LOGIN"})
    private void login(@RequestBody UserForm user){
        userService.login(user);
        httpSession.setAttribute("USER", user);
    }
}
