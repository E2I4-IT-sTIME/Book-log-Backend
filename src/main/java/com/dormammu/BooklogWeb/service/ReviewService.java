package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.review.ReviewRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final PortfolioRepository portfolioRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewListRes myReviewList(User user) {
        List<Review> reviewList = reviewRepository.findByUserId(user.getId());

        List<ReviewRes> reviewResList = new ArrayList<>();

        for (Review rl : reviewList) {
            ReviewRes reviewRes = ReviewRes.builder()
                    .title(rl.getTitle())
                    .content(rl.getContent())
                    .review_id(rl.getId())
                    .book_name(rl.getBook_name())
                    .createDate(rl.getCreateDate()).build();
            reviewResList.add(reviewRes);
        }

        ReviewListRes reviewListRes = ReviewListRes.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .reviewResList(reviewResList).build();
        return reviewListRes;
    }

    @Transactional
    public int createReview(PostReviewReq postReviewReq, User user) {
        Review review = new Review();

        review.setTitle(postReviewReq.getTitle());
        review.setContent(postReviewReq.getContent());
        review.setBook_name(postReviewReq.getBook_name());
        review.setUser(user);
        reviewRepository.save(review);

        return review.getId();
    }

    @Transactional
    public String updateReview(PostReviewReq postReviewReq, int review_id, User user) {
        Review origin_review = reviewRepository.findById(review_id);

        if (origin_review.getUser().getId() == user.getId()) {
            origin_review.setTitle(postReviewReq.getTitle());
            origin_review.setContent(postReviewReq.getContent());
            origin_review.setBook_name(postReviewReq.getBook_name());
            return "서평 수정 완료";
        }
        else {
            return null;
        }
    }

    @Transactional
    public String deleteReview(User user, int review_id) {
        Review review = reviewRepository.findById(review_id);

        if (review.getUser().getId() == user.getId()) {
            reviewRepository.delete(review);
            return "서평 삭제 완료";
        }
        else {
            return null;
        }
    }

    @Transactional
    public List<GetCommunityRes> community() {
        List<GetCommunityRes> getCommunityResList = new ArrayList<>();
        List<Review> reviews = reviewRepository.findAll();
        for (Review r : reviews) {
            GetCommunityRes getCommunityRes = new GetCommunityRes();

            getCommunityRes.setTitle(r.getTitle());
            getCommunityRes.setContent(r.getContent());
            getCommunityRes.setBook_name(r.getBook_name());
            getCommunityRes.setUsername(r.getUser().getUsername());

            getCommunityResList.add(getCommunityRes);
        }
        return getCommunityResList;
    }

    @Transactional
    public GetCommunityRes communityDetail(int review_id) {
        Review review = reviewRepository.findById(review_id);
        GetCommunityRes getCommunityRes = new GetCommunityRes();

        getCommunityRes.setTitle(review.getTitle());
        getCommunityRes.setBook_name(review.getBook_name());
        getCommunityRes.setUsername(review.getUser().getUsername());
        getCommunityRes.setContent(review.getContent());

        return getCommunityRes;
    }

    @Transactional
    public String plusReviewToPortfolio(int portfolio_id, int review_id, User user) {
        Portfolio portfolio = portfolioRepository.findById(portfolio_id);
        Review review = reviewRepository.findById(review_id);

        review.setPortfolio(portfolio);

        return "포트폴리오에 서평 추가 완료";
    }

    @Transactional
    public ReviewRes oneReview(int user_id, int review_id) {
        Review review = reviewRepository.findById(review_id);

        ReviewRes reviewRes = new ReviewRes();

        reviewRes.setTitle(review.getTitle());
        reviewRes.setContent(review.getContent());
        reviewRes.setBook_name(review.getBook_name());
        reviewRes.setReview_id(review.getId());
        reviewRes.setCreateDate(review.getCreateDate());

        return reviewRes;
    }
}
