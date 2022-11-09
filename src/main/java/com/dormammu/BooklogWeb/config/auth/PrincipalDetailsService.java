package com.dormammu.BooklogWeb.config.auth;

import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정(SecurityConfig)에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 함수가 실행
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 파라미터에 있는 (String username) 정확히 사용해야 함.
    // User 필드에 만약 username이 아닌, username2 라고 사용한다면
    // SecurityConfig 에서 .usernameParameter("username2") 를 설정해야 함.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadUserByUsername()");
        User userEntity = userRepository.findByEmail(username);
        // Security Session 안에 => Authentication 안에 => UserDetails(PrincipalDetails)
        // 즉, User를 넣은 UserDetails(PrincipalDetails)를 리턴하는 과정
        // 리턴하면 Authentication 내부에 UserDetails가 들어감
        return new PrincipalDetails(userEntity);
    }
}