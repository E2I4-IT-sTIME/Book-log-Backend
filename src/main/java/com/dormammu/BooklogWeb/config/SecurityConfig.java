package com.dormammu.BooklogWeb.config;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.config.auth.PrincipalDetailsService;
import com.dormammu.BooklogWeb.config.jwt.JwtAuthenticationFilter;
import com.dormammu.BooklogWeb.config.jwt.JwtAuthorizationFilter;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

// EnableGlobalMethodSecurity 어노티에션 에서
// securedEnabled=true : Secured 어노테이션 활성화 (Controller에서 특정 메소드에 @Secured("ROLE_ADMIN") 처럼 권한 하나를 간단하게 걸 수 있도록)
// prePostEnabled=true : PreAuthorize 어노테이션, postAuthorize 어노테이션 활성화 (Controller에서 @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 처럼 권한 여러 개 걸 수 있도록)

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .mvcMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/**", "/v2/api-docs")
//                .anyRequest();
//    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new CustomFilter())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**")
                .access("hasRole('ROLE_USER')")
                .anyRequest().permitAll()
                .and()
                .build();
    }

    public class CustomFilter extends AbstractHttpConfigurer<CustomFilter, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter)
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
}