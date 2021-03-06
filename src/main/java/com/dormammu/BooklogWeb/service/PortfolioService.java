package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            getPortfolioListRes.setPortfolio_id(pf.getId());

            getPortfolioListResList.add(getPortfolioListRes);
        }
        return getPortfolioListResList;
    }

    @Transactional
    public int createPortfolio(User user, PostPortfolioReq portfolioReq) {
        Portfolio portfolio = new Portfolio();
        portfolio.setTitle(portfolioReq.getTitle());
        portfolio.setContent(portfolioReq.getContent());
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);

        return portfolio.getId();
    }

    @Transactional
    public String updatePortfolio(User user, PostPortfolioReq portfolioReq, int p_id) {
        Portfolio origin_portfolio = portfolioRepository.findById(p_id);
        if (origin_portfolio.getUser().getId() == user.getId()) {
            origin_portfolio.setTitle(portfolioReq.getTitle());
            origin_portfolio.setContent(portfolioReq.getContent());
            return "??????????????? ?????? ??????";
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
            return "??????????????? ?????? ??????";
        }
        else {
            return null;
        }
    }

    @Transactional
    public GetPortfolioRes onePortfolio(int user_id, int portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id);

        List<Review> reviews = portfolio.getReviews();

        List<ReviewRes> reviewResList = new ArrayList<>();

        for (Review r : reviews) {
            ReviewRes reviewRes = ReviewRes.builder()
                    .title(r.getTitle())
                    .review_id(r.getId())
                    .content(r.getContent())
                    .book_name(r.getBook_name())
                    .createDate(r.getCreateDate()).build();
            reviewResList.add(reviewRes);
        }

        GetPortfolioRes getPortfolioRes = GetPortfolioRes.builder()
                .title(portfolio.getTitle())
                .content(portfolio.getContent())
                .reviewResList(reviewResList).build();

        return getPortfolioRes;
    }
}
