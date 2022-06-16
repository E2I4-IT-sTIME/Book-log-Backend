package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.review.ReviewRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
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
}
