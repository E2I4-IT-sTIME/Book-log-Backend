package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.GetPortfolioListRes;
import com.dormammu.BooklogWeb.dto.GetPortfolioRes;
import com.dormammu.BooklogWeb.dto.PostPortfolioReq;
import com.dormammu.BooklogWeb.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    // 내 포트폴리오 조회 페이지
    @GetMapping("/auth/user/{id}/portfolios")
    public List<GetPortfolioListRes> myPortfolioList(@PathVariable int id, Authentication authentication) {
        User user = userRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (user.getId() == principalDetails.getUser().getId()) {
            return portfolioService.myPortfolioList(user);
        }
        return null;
    }

    // 포트폴리오 생성
    @PostMapping("/auth/portfolio")
    public String createPortfolio(@RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        return "portfolio_id : " + portfolioService.createPortfolio(principalDetails.getUser(), postPortfolioReq);
    }

    // 포트폴리오 수정
    @PatchMapping("/auth/portfolio/{p_id}")
    public String updatePortfolio(@RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication, @PathVariable int p_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        portfolioService.updatePortfolio(principalDetails.getUser(), postPortfolioReq, p_id);

        return "포트폴리오 수정 완료";

    }

    // 포트폴리오 삭제
    @DeleteMapping("/auth/portfolio/{p_id}")
    public String deletePortfolio(Authentication authentication, @PathVariable int p_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        portfolioService.deletePortfolio(principalDetails.getUser(), p_id);

        return "포트폴리오 삭제 완료";

    }

    // 포트폴리오 조회 (개별)
    @GetMapping("/auth/user/{id}/portfolios/{portfolio_id}")
    public GetPortfolioRes onePortfolio(Authentication authentication, @PathVariable int id, @PathVariable int portfolio_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (principalDetails.getUser().getId() == id) {
            GetPortfolioRes getPortfolioRes = portfolioService.onePortfolio(id, portfolio_id);
            return getPortfolioRes;
        }
        else {
            return null;
        }
    }

}
