//package com.dormammu.BooklogWeb.config;
//
//import com.dormammu.BooklogWeb.config.jwt.CustomAuthenticationEntryPoint;
//import com.dormammu.BooklogWeb.config.jwt.JwtAuthorizationFilter;
//import com.dormammu.BooklogWeb.domain.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.filter.CorsFilter;
//
//import javax.servlet.Filter;
//
//// EnableGlobalMethodSecurity 어노티에션 에서
//// securedEnabled=true : Secured 어노테이션 활성화 (Controller에서 특정 메소드에 @Secured("ROLE_ADMIN") 처럼 권한 하나를 간단하게 걸 수 있도록)
//// prePostEnabled=true : PreAuthorize 어노테이션, postAuthorize 어노테이션 활성화 (Controller에서 @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 처럼 권한 여러 개 걸 수 있도록)
//
//@EnableWebSecurity
//@RequiredArgsConstructor
//@Configuration
//public class SecurityConfig2 extends WebSecurityConfigurerAdapter {
//
//    private final CorsFilter corsFilter;
//    private final UserRepository userRepository;
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/swagger-ui/**", "/swagger-ui.html");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        Filter filter = new JwtAuthorizationFilter(authenticationManager(), userRepository);
//
//        http.csrf().disable()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .authorizeRequests()
//                .antMatchers("/auth/**")
//                .access("hasRole('ROLE_USER')")
//                .anyRequest().permitAll()
//                .and()
//                .addFilter(corsFilter)
//                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//}