package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.spel.ast.PropertyOrFieldReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public List<GetPortfolioListRes> myPortfolioList(User user) {
        List<Portfolio> portfolioList = portfolioRepository.findByUser(user);

        List<GetPortfolioListRes> getPortfolioListResList = new ArrayList<>();

        for (Portfolio pf : portfolioList) {
            GetPortfolioListRes getPortfolioListRes = new GetPortfolioListRes();
            getPortfolioListRes.setTitle(pf.getTitle());
            getPortfolioListRes.setContent(pf.getContent());

            getPortfolioListResList.add(getPortfolioListRes);
        }

        return getPortfolioListResList;
    }

    @Transactional
    public String createPortfolio(User user, PostPortfolioReq portfolioReq) {
        Portfolio portfolio = new Portfolio();
        portfolio.setTitle(portfolioReq.getTitle());
        portfolio.setContent(portfolioReq.getContent());
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);
        return "포트폴리오 생성 완료";
    }

    @Transactional
    public String updatePortfolio(User user, PostPortfolioReq portfolioReq, int p_id) {
        Portfolio origin_portfolio = portfolioRepository.findById(p_id);
        if (origin_portfolio.getUser().getId() == user.getId()) {
            origin_portfolio.setTitle(portfolioReq.getTitle());
            origin_portfolio.setContent(portfolioReq.getContent());
            return "포트폴리오 수정 완료";
        }
        else {
            return null;
        }
    }

    @Transactional
    public String deletePortfolio(User user, int p_id) {
        Portfolio portfolio = portfolioRepository.findById(p_id);
        if (portfolio.getUser().getId() == user.getId()) {
            portfolioRepository.delete(portfolio);
            return "포트폴리오 삭제 완료";
        }
        else {
            return null;
        }
    }

    @Transactional
    public GetPortfolioRes onePortfolio(int user_id, int portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id);
        User user = userRepository.findById(user_id);

        List<Review> reviews = portfolio.getReviews();

        List<ReviewRes> reviewResList = new ArrayList<>();

        for (Review r : reviews) {
            ReviewRes reviewRes = ReviewRes.builder()
                    .title(r.getTitle())
                    .content(r.getContent())
                    .book_name(r.getBook_name())
                    .createDate(r.getCreateDate()).build();
            reviewResList.add(reviewRes);
        }

        GetPortfolioRes getPortfolioRes = GetPortfolioRes.builder()
                .title(portfolio.getTitle())
                .reviewResList(reviewResList).build();

        return getPortfolioRes;
    }
}
