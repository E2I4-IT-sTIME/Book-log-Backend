package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.jwt.JwtProperties;
import com.dormammu.BooklogWeb.dto.LoginRes;
import com.dormammu.BooklogWeb.service.AuthService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
