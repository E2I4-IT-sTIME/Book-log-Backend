package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.review.PortfolioReview;
import com.dormammu.BooklogWeb.domain.review.PortfolioReviewRepository;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.review.ReviewRepository;
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
    private final ReviewRepository reviewRepository;
    private final PortfolioReviewRepository portfolioReviewRepository;

    @Transactional
    public List<GetPortfolioListRes> myPortfolioList(User user) {
        List<Portfolio> portfolioList = portfolioRepository.findByUser(user);

        List<GetPortfolioListRes> getPortfolioListResList = new ArrayList<>();

        for (Portfolio pf : portfolioList) {

            List<Review> reviews = reviewRepository.findByPortfolioId(pf.getId());
            List<String> review_isbn = new ArrayList<>();

            for (Review r : reviews) {
                review_isbn.add(r.getIsbn());
            }

            GetPortfolioListRes getPortfolioListRes = new GetPortfolioListRes();
            getPortfolioListRes.setTitle(pf.getTitle());
            getPortfolioListRes.setContent(pf.getContent());
            getPortfolioListRes.setImage(pf.getImage());
            getPortfolioListRes.setIsbn(review_isbn);
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
        portfolio.setImage(portfolioReq.getImage());
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);

        if (portfolioReq.getReviews_id() != null) {
            for (int r : portfolioReq.getReviews_id()) {
                Review review = reviewRepository.findById(r);
                PortfolioReview portfolioReview = new PortfolioReview();
                portfolioReview.setPortfolio(portfolio);
                portfolioReview.setReview(review);
                portfolioReviewRepository.save(portfolioReview);
            }
        }
        return portfolio.getId();
    }

    @Transactional
    public String updatePortfolio(User user, PostPortfolioReq portfolioReq, int portfolio_id) {
        Portfolio origin_portfolio = portfolioRepository.findById(portfolio_id);

        if (origin_portfolio.getUser().getId() == user.getId()) {
            origin_portfolio.setTitle(portfolioReq.getTitle());
            origin_portfolio.setContent(portfolioReq.getContent());
            origin_portfolio.setImage(portfolioReq.getImage());

            // 포트폴리오 내의 서평 id 모두 제거
            portfolioReviewRepository.deleteByPortfolioId(origin_portfolio.getId());

            // 전체 다시 등록
            List<Integer> reviews_id = portfolioReq.getReviews_id();
            for (int ri : reviews_id) {
                PortfolioReview portfolioReview = new PortfolioReview();
                portfolioReview.setPortfolio(origin_portfolio);
                portfolioReview.setReview(reviewRepository.findById(ri));

                portfolioReviewRepository.save(portfolioReview);
            }
            return "포트폴리오 수정 완료";
        }
        else {
            return "해당 유저의 권한이 없습니다.";
        }
    }

    @Transactional
    public String deletePortfolio(User user, int portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id);

        if (portfolio.getUser().getId() == user.getId()) {
            portfolioRepository.delete(portfolio);
            return "포트폴리오 삭제 완료";
        }
        else {
            return null;
        }
    }

    @Transactional
    public GetPortfolioRes onePortfolio(User user, int portfolio_id) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id);

        List<Review> reviews = reviewRepository.findByPortfolioId(portfolio_id);

        List<ReviewRes> reviewResList = new ArrayList<>();

        for (Review r : reviews) {
            ReviewRes reviewRes = ReviewRes.builder()
                    .title(r.getTitle())
                    .review_id(r.getId())
                    .content(r.getContent())
                    .isbn(r.getIsbn())
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
