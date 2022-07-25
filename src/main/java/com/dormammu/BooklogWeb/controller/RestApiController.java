package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.JoinRequestDto;
import com.dormammu.BooklogWeb.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

//@RequestMapping("api")
@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final UserService userService;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")

    public String home() {
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    //@PreAuthorize("hasRole('ROLE_ADMIN')") -> 안먹힘
    public String token() {
        return "<h1>token</h1>";
    }

//    @PostMapping("join")  // 회원가입 API (+이미지)
//    public String join(@Valid @RequestPart(value = "joinRequestDto") JoinRequestDto joinRequestDto, @RequestPart(value = "image") MultipartFile multipartFile, Errors errors) throws IOException {
//        if (errors.hasErrors()) {
//            Map<String, String> validatorResult = userService.validateHandling(errors);
//            for (String key : validatorResult.keySet()) {
//                System.out.println(validatorResult.get(key));
//                return validatorResult.get(key);
//            }
//        }
//        userService.checkEmailDuplication(joinRequestDto);
//        joinRequestDto.setPassword(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()));
//        joinRequestDto.setRoles("ROLE_USER");
//        userService.joinUser(joinRequestDto, multipartFile);
//        return "회원가입완료";
//    }

    @PostMapping("join")
    public String join(@Valid @RequestBody JoinRequestDto joinRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                System.out.println(validatorResult.get(key));
                return validatorResult.get(key);
            }
        }
        userService.checkEmailDuplication(joinRequestDto);
        joinRequestDto.setPassword(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()));
        joinRequestDto.setRoles("ROLE_USER");
        userService.joinUser(joinRequestDto);
        return "회원가입완료";
    }

    // 회원 탈퇴
    @PatchMapping("/auth/user/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "회원탈퇴완료";
    }

    // user, admin 가능
    @GetMapping("/api/user")
    public String user(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : " + principalDetails.getUsername());
        return "user";
    }

    // admin만 가능
    @GetMapping("/api/admin")
    public String admin() {
        return "admin";
    }

}