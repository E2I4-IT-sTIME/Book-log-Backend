package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.jwt.JwtProperties;
import com.dormammu.BooklogWeb.dto.LoginRes;
import com.dormammu.BooklogWeb.service.AuthService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    // 인가코드 과정 없이 바로 액세스코드 받아오기
    @GetMapping("/api/access_token")
    @ApiOperation(value = "엑세스 코드 받기 API")
    @ApiImplicitParam(name = "token", value = "엑세스 코드", required = true, dataType = "string")
    public ResponseEntity getToken(@RequestParam("token") String token) {

        LoginRes loginRes = authService.saveUser(token);

        System.out.println("jwtToken : Bearer " + loginRes.getJwtToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + loginRes.getJwtToken());

        return ResponseEntity.ok().headers(headers).body(loginRes);
    }

    @PostMapping("/join/{id}/username")
    @ApiOperation(value = "추가정보입력(닉네임) API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "유저 아이디", paramType = "path", required = true, dataType = "long"),
            @ApiImplicitParam(name = "name", value = "유저 이름", required = true, dataType = "string")
    })
    public void addInfoName(@PathVariable int id, @RequestParam("name") String name) {
        authService.addUsername(id, name);
    }
}
