package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.dto.GetUserInfoRes;
import com.dormammu.BooklogWeb.dto.GetUserRes;
import com.dormammu.BooklogWeb.service.S3Uploader;
import com.dormammu.BooklogWeb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;

@RestController
@RequiredArgsConstructor
@Api(tags = {"유저 API"})  // Swagger 최상단 Controller 명칭
public class UserController {
    private final UserService userService;

    @PatchMapping("/auth/user/{id}")  // 회원정보 수정 api
    public String updateUser(@RequestPart(value = "image", required = false) MultipartFile multipartFile, @PathVariable int id, @RequestParam(name = "username", required = false) String username, Authentication authentication) throws IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        userService.updateUser(multipartFile, id, username, principalDetails.getUser());

        return "회원수정완료";
    }

    @ApiOperation(value = "마이페이지", notes = "마이페이지 api")
    @ApiImplicitParam(name = "id", value = "유저 id값")
    @GetMapping("/auth/user/{id}")  // 마이페이지 api
    public GetUserRes myPage(@PathVariable int id, Authentication authentication) throws IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        return userService.myPage(id, principalDetails.getUser());
    }

    // 회원 탈퇴
    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴 api")
    @ApiImplicitParam(name = "id", value = "유저 id값")
    @PatchMapping("/auth/user/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "회원탈퇴완료";
    }

    // user_id 제공
    @ApiOperation(value = "user id 제공", notes = "user id 제공 api")
    @GetMapping("/auth/userid")
    public ResponseEntity<Integer> userId(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return new ResponseEntity<>(principalDetails.getUser().getId(), HttpStatus.OK);
    }

    // 이름, 프로필 사진 제공
    @ApiOperation(value = "이름, 프로필 사진 제공", notes = "이름, 프로필 사진 제공 api")
    @ApiImplicitParam(name = "id", value = "유저 id값")
    @GetMapping("/auth/userinfo")
    public ResponseEntity<GetUserInfoRes> userInfo(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        GetUserInfoRes getUserInfoRes = userService.userInfo(principalDetails.getUser());
        return new ResponseEntity<>(getUserInfoRes, HttpStatus.OK);
    }

}