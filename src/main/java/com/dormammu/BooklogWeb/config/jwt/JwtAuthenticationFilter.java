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

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 라는 필터가 있음
// /login 요청이 들어오면, username과 password를 post로 전송하면
// UsernamePasswordAuthenticationFilter가 동작한다
import java.io.IOException;
import java.util.Date;

// 인증
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        // 1. username, password 받기
        ObjectMapper om = new ObjectMapper();  // json데이터를 파싱해준다
        LoginRequestDto loginRequestDto = null;
        try{
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);  // User 객체에 있는 값을 담아준다

            System.out.println("JwtAuthenticationFilter : "+loginRequestDto);
        } catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter의 attemptAuthentication() : "+loginRequestDto);

        // 토큰 만들기
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        System.out.println("JwtAuthenticationFilter : 토큰생성완료");


        // PrincipalDetailsService의 loadUserByUsername()함수가 실행된다
        // authentication에 내가 로그인한 정보가 담긴다
        // DB에 있는 username과 password가 일치한다
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);  // 토큰을 날려준다

        // authentication객체가 session영역에 저장된다. => 로그인이 되었음
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("로그인 완료됨 : " + principalDetails.getUser().getEmail());  // 로그인이 정상적으로 되었음

        // 리턴의 이유는 권한관리를sercuriy가 대신 해주기에 편함.
        // 굳이 JWT 토큰을 사용하면서 세션을 만들 필요가 없지만, 권한처리의 문제때문에 session에 넣어준다.
        return authentication;
    }

    // attemptAuthentication 실행 후, 인증이 정상적으로 되었으면 successfulAuthentication함수가 실행된다
    // JWT토큰을 만들어서 request를 요청한 사용자에세 JWT토큰을 reponse해준다
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었음");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA방식X. Hash암호방식
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))  // 만료시간 1000이 1초. 1분*10
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("email", principalDetails.getUser().getEmail())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));  // 내 서버만 아는 고유한 값

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
    }
}