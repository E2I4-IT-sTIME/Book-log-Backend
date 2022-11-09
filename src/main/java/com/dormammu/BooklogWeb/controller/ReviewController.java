package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import com.dormammu.BooklogWeb.service.ReviewService;
import com.dormammu.BooklogWeb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    // 내가 작성한 서평
    @GetMapping("/auth/user/{user_id}/reviews")
    public List<ReviewRes> myReviewList(@PathVariable int user_id, Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        List<ReviewRes> reviewResList = reviewService.myReviewList(user);

        return reviewResList;
    }

    // 서평 생성
    @PostMapping("/auth/user/{user_id}/review")
    public String createReview(@PathVariable int user_id, @RequestBody PostReviewReq postReviewReq, Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        int review_id = reviewService.createReview(postReviewReq, principalDetails.getUser());
        return "review_id : " + review_id;
    }

    // 서평 수정
    @PatchMapping("/auth/user/{user_id}/review/{review_id}")
    public Boolean updateReview(@PathVariable int user_id, @RequestBody PostReviewReq postReviewReq, Authentication authentication, @PathVariable int review_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        reviewService.updateReview(postReviewReq, review_id, principalDetails.getUser());
        return true;
    }

    // 서평 삭제
    @DeleteMapping("/auth/user/{user_id}/review/{review_id}")
    public Boolean deleteReview(@PathVariable int user_id, Authentication authentication, @PathVariable int review_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }
        reviewService.deleteReview(principalDetails.getUser(), review_id);

        return true;
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
    @PostMapping("/auth/user/{user_id}/portfolio/{portfolio_id}/review/{review_id}")
    public Boolean plusReviewToPortfolio(@PathVariable int user_id, @PathVariable int portfolio_id, @PathVariable int review_id, Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        reviewService.plusReviewToPortfolio(portfolio_id, review_id);

        return true;
    }

    // 서평 조회 (개별)
    @GetMapping("/auth/user/{user_id}/review/{review_id}")
    public ReviewRes oneReview(Authentication authentication, @PathVariable int user_id, @PathVariable int review_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        ReviewRes reviewRes = reviewService.oneReview(user, review_id);
        return reviewRes;
    }
}
