package com.backend.dove.api;

import com.backend.dove.dto.UserInfoDto;
import com.backend.dove.dto.UserLoginDto;
import com.backend.dove.dto.UserRegisterDto;
import com.backend.dove.entity.User;
import com.backend.dove.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.IOException;

@RestController("User Controller")
@RequestMapping("/api/user/")
public class UserController {

    private PasswordEncoder passwordEncoder;

    private UserService service;

    public UserController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("login")
    @ResponseBody
    public UserInfoDto login(@RequestBody UserLoginDto loginDto) throws Exception {
        return service.login(loginDto);
    }

    @PostMapping("logout")
    @Secured("USER")
    public void logout() {
        service.logout();
    }

    @PostMapping("admin/register")
    @Secured("ADMIN")
    @ResponseBody
    public String register_admin(@RequestBody UserRegisterDto registerDto) throws IOException {
        service.register(registerDto, User.Role.ADMIN);

        return "registered new admin " + registerDto.getUsername();
    }

    @PostMapping("register")
    @ResponseBody
    public String register(@RequestBody UserRegisterDto registerDto) throws IOException {
        service.register(registerDto, User.Role.USER);

        return "registered new user " + registerDto.getUsername();
    }

    @GetMapping("validate")
    public void validate(
            @PathParam("token") String token,
            @PathParam("email") String email
    ) {
        service.validation(email, token);
    }

}