package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.model.user.User;
import com.dormammu.BooklogWeb.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }

    // user, admin 권한 접근가능
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    // admin만 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }

    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");  // ROLE은 기본으로 설정해주기
        userRepository.save(user);
        return "회원가입완료";
    }
}
