package com.dormammu.BooklogWeb.config.auth;

import com.dormammu.BooklogWeb.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행이 완료되면 시큐리티 session을 만들어준다. (Security ContextHolder)
// Session에 들어갈 수 있는 정보는 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 한다.
// 여기서 User 오브젝트 타입 => UserDetails 타입 객체

// Security Session(시큐리티 세션 영역) 안에 => Authentication 객체가 있음. 이 안에 User 정보를 저장하는 => UserDetails(PrincipalDetails) 타입 객체가 있음. 이 안에서 => User 정보 객체 접근 가능
// UserDetails 객체는 PrincipalDetails에서,
// Authentication을 만들기 위해서는 PrincipalDetailsService 생성

@Data
public class PrincipalDetails implements UserDetails {

    private User user; // 컴포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 리턴 타입이 Collection<? extends GrantedAuthority> 임.
    // 해당 User의 권한을 리턴하는 곳
    // user.getRole을 Collection<? extends GrantedAuthority> 타입으로 만들기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collector = new ArrayList<>(); // ArrayList는 Collection의 자식
        collector.add(() ->{ return user.getRoles();});

        return collector;

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}