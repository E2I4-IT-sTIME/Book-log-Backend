package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.model.Portfolio;
import com.dormammu.BooklogWeb.model.User;
import com.dormammu.BooklogWeb.repository.PortfolioService;
import com.dormammu.BooklogWeb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PortfolioService portfolioService;

    // 내 포트폴리오 조회 페이지
    @GetMapping("/api/v1/user/{id}/porfols")
    public List<Portfolio> porfolList(@PathVariable Long id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("현재 로그인된 유저 : " + principalDetails.getUser().getUsername() + principalDetails.getUser().getNickname());
        //List<Portfolio> portfolios =
        return portfolioService.portfolList(principalDetails.getUser());
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public String home() {
        return "Hello";
    }
}
