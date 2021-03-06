package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
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
    @GetMapping("/auth/user/{id}/reviews")
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
    @PostMapping("/auth/review")
    public String createReview(@RequestBody PostReviewReq postReviewReq, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        return "review_id : " + reviewService.createReview(postReviewReq, principalDetails.getUser());
    }

    // 서평 수정
    @PatchMapping("/auth/review/{review_id}")
    public String updateReview(@RequestBody PostReviewReq postReviewReq, Authentication authentication, @PathVariable int review_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        reviewService.updateReview(postReviewReq, review_id, principalDetails.getUser());
        return "서평 수정 완료";
    }

    // 서평 삭제
    @DeleteMapping("/auth/review/{review_id}")
    public String deleteReview(Authentication authentication, @PathVariable int review_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        reviewService.deleteReview(principalDetails.getUser(), review_id);

        return "서평 삭제 완료";
    }

    // 커뮤니티 전체 페이지 (모든 사람들의 서평 리스트)
    @GetMapping("/community")
    public List<GetCommunityRes> community(Authentication authentication) {
        List<GetCommunityRes> getCommunityRes = reviewService.community();
        return getCommunityRes;
    }

    // 커뮤니티 상세 페이지 (한 사람의 서평)
    @GetMapping("/auth/reviews/{review_id}")
    public GetCommunityRes communityDetail(Authentication authentication, @PathVariable int review_id) {
        GetCommunityRes getCommunityRes = reviewService.communityDetail(review_id);
        return getCommunityRes;
    }

    // 서평 추가 버튼
    @PostMapping("/auth/{portfolio_id}/review/{review_id}")
    public String plusReviewToPortfolio(@PathVariable int portfolio_id, @PathVariable int review_id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        reviewService.plusReviewToPortfolio(portfolio_id, review_id, principalDetails.getUser());
        return "포트폴리오에 서평 추가 완료";
    }

    // 서평 조회 (개별)
    @GetMapping("/auth/user/{id}/review/{review_id}")
    public ReviewRes oneReview(Authentication authentication, @PathVariable int id, @PathVariable int review_id) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (principalDetails.getUser().getId() == id) {
            ReviewRes reviewRes = reviewService.oneReview(id, review_id);
            return reviewRes;
        }
        else {
            return null;
        }
    }
}
