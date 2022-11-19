package com.dormammu.BooklogWeb.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dormammu.BooklogWeb.config.jwt.JwtProperties;
import com.dormammu.BooklogWeb.config.oauth.AccessTokenRes;
import com.dormammu.BooklogWeb.config.oauth.KakaoProfile;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.LoginRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    // 환경 변수
    @Value("${spring.jpa.security.oauth2.client.registration.kakao.client-id}")
    String client_id;

    @Value("${spring.jpa.security.oauth2.client.registration.kakao.client-secret}")
    String client_secret;

    @Value("${spring.jpa.security.oauth2.client.registration.kakao.redirect-uri}")
    String redirect_uri;

    @Transactional
    public AccessTokenRes getAccessToken(String code) {

        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);
        // params.add("client_secret", "{시크릿 키}"); // 우선 생략

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        AccessTokenRes accessTokenRes = null;
        try {
            accessTokenRes = objectMapper.readValue(accessTokenResponse.getBody(), AccessTokenRes.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return accessTokenRes;
    }

    // accessToken 으로 회원정보 요청 후 DB에 저장
    @Transactional
    public LoginRes saveUser(String token) {
        boolean isExist = true;

        // 카카오 서버로부터 회원정보 받아오기
        KakaoProfile profile = findProfile(token);

        User user = userRepository.findByEmail(profile.getKakao_account().getEmail());

        if(user == null) {
            isExist = false;
            user = User.builder()
                    .username(profile.getKakao_account().getProfile().getNickname())
                    .email(profile.getKakao_account().getEmail())
                    .imgPath(profile.getKakao_account().getProfile().getProfile_image_url()).build();

            userRepository.save(user);
        }

        return createToken(user, isExist);
    }

    @Transactional
    public KakaoProfile findProfile(String token) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    @Transactional
    public LoginRes createToken(User user, Boolean isExist) {

        String jwtToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))

                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())

                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        LoginRes loginRes = LoginRes.builder()
                .jwtToken(jwtToken)
                .isExist(isExist)
                .userId(user.getId()).build();
        return loginRes;
    }




}
