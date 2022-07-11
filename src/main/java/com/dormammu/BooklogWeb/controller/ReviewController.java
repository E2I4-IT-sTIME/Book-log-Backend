package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostReviewReq;
import com.dormammu.BooklogWeb.dto.ReviewListRes;
import com.dormammu.BooklogWeb.service.PortfolioService;
import com.dormammu.BooklogWeb.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    // 내 서평 조회 페이지
    @GetMapping("/api/v1/user/{id}/reviews")
    public ReviewListRes myReviewList(@PathVariable int id, Authentication authentication) {
        User user = userRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (user.getId() == principalDetails.getUser().getId()) {
            System.out.println("현재 로그인된 유저 : " + principalDetails.getUser().getUsername() + "&&&&&" + user.getUsername());
            return reviewService.myReviewList(user);
        }
        return null;
    }

    // 서평 생성
    @PostMapping("auth/review")
    public String createReview(@RequestBody PostReviewReq postReviewReq, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (postReviewReq.getUserId() == principalDetails.getUser().getId()) {
            reviewService.createReview(postReviewReq, principalDetails.getUser());
            return "서평 생성 완료";
        }
        return null;
    }

    // 서평 수정
    @PatchMapping("auth/review/{review_id}")
    public String updateReview(@RequestBody PostReviewReq postReviewReq, Authentication authentication, @PathVariable int review_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (postReviewReq.getUserId() == principalDetails.getUser().getId()) {
            reviewService.updateReview(postReviewReq, review_id);
            return "서평 생성 완료";
        }
        return null;
    }

    // 서평 삭제
}
