package com.dormammu.BooklogWeb.config;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.config.auth.PrincipalDetailsService;
import com.dormammu.BooklogWeb.config.jwt.JwtAuthenticationFilter;
import com.dormammu.BooklogWeb.config.jwt.JwtAuthorizationFilter;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

// EnableGlobalMethodSecurity 어노티에션 에서
// securedEnabled=true : Secured 어노테이션 활성화 (Controller에서 특정 메소드에 @Secured("ROLE_ADMIN") 처럼 권한 하나를 간단하게 걸 수 있도록)
// prePostEnabled=true : PreAuthorize 어노테이션, postAuthorize 어노테이션 활성화 (Controller에서 @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 처럼 권한 여러 개 걸 수 있도록)

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.addFilterAfter(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 사용X, STATELESS 서버
                .and()
                .addFilter(corsFilter)  // 시큐리티 필터에 등록 인증, cors 정책에서 벗어나 모든 요청 허용, @CrossOrigin은 인증이 없을 때 사용하는데, 인증이 있을 때는 시큐리티 필터에 등록을 해주어야 함.
                .formLogin().disable() // 폼(폼태그) 로그인 사용하지 않음
                .httpBasic().disable() // Basic 방식은 ID, PW를 들고 오는 방식이라 위험함, Bearer 방식은 Token을 들고 오기 때문에 노출 되어도 조금 더 안전
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))  // AuthenticationManager
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .antMatchers("/api/user/**", "/auth/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

    }

//    private final PrincipalDetailsService principalDetailsService;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(principalDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }
}