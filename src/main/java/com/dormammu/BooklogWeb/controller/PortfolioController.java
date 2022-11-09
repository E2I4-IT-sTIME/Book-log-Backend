package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.GetPortfolioListRes;
import com.dormammu.BooklogWeb.dto.GetPortfolioRes;
import com.dormammu.BooklogWeb.dto.PostPortfolioReq;
import com.dormammu.BooklogWeb.service.PortfolioService;
import com.dormammu.BooklogWeb.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserService userService;

    // 내 포트폴리오 조회 페이지
    @GetMapping("/auth/user/{user_id}/portfolios")
    public List<GetPortfolioListRes> myPortfolioList(@PathVariable int user_id, Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }
        return portfolioService.myPortfolioList(user);
    }

    // 포트폴리오 생성
    @PostMapping("/auth/user/{user_id}/portfolio")
    public Boolean createPortfolio(@PathVariable int user_id, @RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        portfolioService.createPortfolio(user, postPortfolioReq);

        return true;
    }

    // 포트폴리오 수정
    @PatchMapping("/auth/user/{user_id}/portfolio/{portfolio_id}")
    public Boolean updatePortfolio(@RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication, @PathVariable int user_id, @PathVariable int portfolio_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        portfolioService.updatePortfolio(user, postPortfolioReq, portfolio_id);

        return true;
    }

    // 포트폴리오 삭제
    @DeleteMapping("/auth/user/{user_id}/portfolio/{portfolio_id}")
    public Boolean deletePortfolio(Authentication authentication, @PathVariable int user_id, @PathVariable int portfolio_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        portfolioService.deletePortfolio(user, portfolio_id);

        return true;
    }

    // 포트폴리오 조회 (개별)
    @GetMapping("/auth/user/{user_id}/portfolios/{portfolio_id}")
    public GetPortfolioRes onePortfolio(Authentication authentication, @PathVariable int user_id, @PathVariable int portfolio_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        GetPortfolioRes getPortfolioRes = portfolioService.onePortfolio(user, portfolio_id);

        return getPortfolioRes;
    }

}
