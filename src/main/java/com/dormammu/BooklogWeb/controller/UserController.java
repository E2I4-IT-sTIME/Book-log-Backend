package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.service.S3Uploader;
import com.dormammu.BooklogWeb.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;

//@RequestMapping("api")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final S3Uploader s3Uploader;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("join")  // 회원가입 API (+이미지)
    public String join(@RequestPart(value = "image") MultipartFile multipartFile, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password,
        @RequestParam(name = "email") String email, @RequestParam(name = "birthday") Date birthday, @RequestParam(name = "job") String job) throws IOException {

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .birthday(birthday)
                .job(job)
                .active(true)
                .roles("ROLE_USER").build();
        System.out.println("유저");
        userService.joinUser(user);

        String r = s3Uploader.uploadProfile(user.getId(), multipartFile, "user");
        System.out.println(r);
        return "회원가입완료";
    }

//    @PostMapping("join")
//    public String join(@Valid @RequestBody JoinRequestDto joinRequestDto, Errors errors) {
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
//        userService.joinUser(joinRequestDto);
//        return "회원가입완료";
//    }

    // 회원 탈퇴
    @PatchMapping("/auth/user/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "회원탈퇴완료";
    }

}