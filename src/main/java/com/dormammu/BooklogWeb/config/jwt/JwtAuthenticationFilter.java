package com.dormammu.BooklogWeb.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 인증

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // 인증 요청 시에 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 진입");
        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;

        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter의 attemptAuthentication() : "+loginRequestDto);

        //User user = om.readValue(request.getInputStream(), User.class);
        //System.out.println(user);

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        System.out.println("JwtAuthenticationFilter : 토큰생성완료");

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("로그인 완료됨 : " + principalDetails.getUser().getUsername());

        return authentication;
    }

    // jwt 토큰 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증 완료됨");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
