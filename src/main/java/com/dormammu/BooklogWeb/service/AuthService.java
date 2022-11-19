package com.dormammu.BooklogWeb.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dormammu.BooklogWeb.config.jwt.JwtProperties;
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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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

    // accessToken 으로 회원정보 요청 후 DB에 저장
    @Transactional
    public LoginRes saveUser(String token) {
        boolean isExist = true;

        // 카카오 서버로부터 회원정보 받아오기
        KakaoProfile profile = findProfile(token);

        // User user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        User user = userRepository.findByKakao_id(profile.id);

        if(user == null) {
            isExist = false;
            user = User.builder()
                    .kakao_username(profile.getKakao_account().getProfile().getNickname())
                    .email(profile.getKakao_account().getEmail())
                    .imgPath(profile.getKakao_account().getProfile().getProfile_image_url())
                    .kakao_id(profile.id).build();

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
                .withSubject(user.getKakao_id().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))

                .withClaim("id", user.getId())
                .withClaim("kakaoId", user.getKakao_id())

                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        LoginRes loginRes = LoginRes.builder()
                .jwtToken(jwtToken)
                .isExist(isExist)
                .userId(user.getId()).build();
        return loginRes;
    }

    @Transactional
    public void addUsername(int id, String name){
        User user = userRepository.findById(id);
        user.setUsername(name);
    }
}
