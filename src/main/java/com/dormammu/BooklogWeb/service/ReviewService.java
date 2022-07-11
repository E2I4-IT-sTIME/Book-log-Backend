package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.review.ReviewRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostReviewReq;
import com.dormammu.BooklogWeb.dto.ReviewListRes;
import com.dormammu.BooklogWeb.dto.ReviewRes;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewListRes myReviewList(User user) {
        System.out.println("ReviewService 들어옴!");
        List<Review> reviewList = reviewRepository.findByUserId(user.getId());
        System.out.println("레퍼지토리로 서평 리스트 출력 : " + reviewList);

        List<ReviewRes> reviewResList = new ArrayList<>();

        for (Review rl : reviewList) {
            ReviewRes reviewRes = ReviewRes.builder()
                    .title(rl.getTitle())
                    .content(rl.getContent())
                    .createDate(rl.getCreateDate()).build();
            reviewResList.add(reviewRes);
        }

        ReviewListRes reviewListRes = ReviewListRes.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .reviewResList(reviewResList).build();
        return reviewListRes;
    }

    @Transactional
    public String createReview(PostReviewReq postReviewReq, User user) {
        Review review = new Review();
        review.setTitle(postReviewReq.getTitle());
        review.setContent(postReviewReq.getContent());
        review.setBook_name(postReviewReq.getBook_name());
        review.setUser(user);
        reviewRepository.save(review);

        return "서평 생성 완료";
    }

    @Transactional
    public String updateReview(PostReviewReq postReviewReq, int review_id) {
        Review origin_review = reviewRepository.findById(review_id);
        if (origin_review.getUser().getId() == postReviewReq.getUserId()) {
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
}
