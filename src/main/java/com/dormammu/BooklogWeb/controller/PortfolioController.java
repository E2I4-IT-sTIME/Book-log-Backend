package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.GetPortfolioListRes;
import com.dormammu.BooklogWeb.dto.GetPortfolioRes;
import com.dormammu.BooklogWeb.dto.PostPortfolioReq;
import com.dormammu.BooklogWeb.service.PortfolioService;
import com.dormammu.BooklogWeb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"포트폴리오 API"})
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserService userService;

    // 내 포트폴리오 조회 페이지
    @ApiOperation(value = "내 포트폴리오 조회", notes = "내 포트폴리오 조회 API 입니다.")
    //@ApiImplicitParam(name = "user_id", value = "포트폴리오를 조회하고자 하는 유저의 고유 id", dataTypeClass = Integer.class)
    @GetMapping("/auth/user/{user_id}/portfolios")
    public List<GetPortfolioListRes> myPortfolioList(@PathVariable int user_id, Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }
        return portfolioService.myPortfolioList(user);
    }

    // 포트폴리오 생성 (+이미지)
    @ApiOperation(value = "포트폴리오 생성", notes = "포트폴리오 생성 API 입니다. 이미지(form-data), 제목+내용(json) 입니다.")
//    @ApiImplicitParams(
//            {@ApiImplicitParam(name = "user_id", value = "포트폴리오를 조회하고자 하는 유저의 고유 id", dataTypeClass = Integer.class),
//                    @ApiImplicitParam(name = "image", value = "포트폴리오 이미지", dataTypeClass = MultipartFile.class),
//                    @ApiImplicitParam(name = "title", value = "포트폴리오 제목", dataTypeClass = String.class),
//                    @ApiImplicitParam(name = "content", value = "포트폴리오 내용", dataTypeClass = String.class),
//                    @ApiImplicitParam(name = "reviews_id", value = "포트폴리오에 들어가는 리뷰의 id 리스트", dataTypeClass = Integer.class),
//
//            }
//    )
    @PostMapping("/auth/user/{user_id}/portfolio")
    public Boolean createPortfolio(@PathVariable int user_id, @RequestPart(value = "image") MultipartFile multipartFile,
                                   @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("reviews_id") List<Integer> reviews_id,
                                   Authentication authentication) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        portfolioService.createPortfolio(user, multipartFile, title, content, reviews_id);

        return true;
    }

    // 포트폴리오 수정
    @ApiOperation(value = "포트폴리오 수정 (아직 작업 중입니다)", notes = "포트폴리오 수정 API 입니다.")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "user_id", value = "포트폴리오를 조회하고자 하는 유저의 고유 id", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name= "portfolio_id", value = "수정하고자 하는 포트폴리오의 고유 id", dataTypeClass = Integer.class)})
    @PatchMapping("/auth/user/{user_id}/portfolio/{portfolio_id}")
    public Boolean updatePortfolio(@RequestBody PostPortfolioReq postPortfolioReq, Authentication authentication, @PathVariable int user_id, @PathVariable int portfolio_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        portfolioService.updatePortfolio(user, postPortfolioReq, portfolio_id);

        return true;
    }

    // 포트폴리오 삭제
    @ApiOperation(value = "포트폴리오 삭제", notes = "포트폴리오 삭제 API 입니다.")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "user_id", value = "포트폴리오를 조회하고자 하는 유저의 고유 id", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name= "portfolio_id", value = "삭제하고자 하는 포트폴리오의 고유 id", dataTypeClass = Integer.class)})
    @DeleteMapping("/auth/user/{user_id}/portfolio/{portfolio_id}")
    public Boolean deletePortfolio(Authentication authentication, @PathVariable int user_id, @PathVariable int portfolio_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        portfolioService.deletePortfolio(user, portfolio_id);

        return true;
    }

    // 포트폴리오 조회 (개별)
    @ApiOperation(value = "포트폴리오 개별 조회", notes = "포트폴리오 개별 조회 API 입니다.")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "user_id", value = "포트폴리오를 조회하고자 하는 유저의 고유 id", dataTypeClass = Integer.class),
//            @ApiImplicitParam(name= "portfolio_id", value = "조회하고자 하는 포트폴리오의 고유 id", dataTypeClass = Integer.class)})
    @GetMapping("/auth/user/{user_id}/portfolios/{portfolio_id}")
    public GetPortfolioRes onePortfolio(Authentication authentication, @PathVariable int user_id, @PathVariable int portfolio_id) throws Exception {
        User user = userService.findUser(user_id);
        if (user == null) {
            throw new Exception("존재하지 않는 유저 id 입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (user.getId() != principalDetails.getUser().getId()) {
            throw new Exception("유저 id가 일치하지 않습니다.");
        }

        GetPortfolioRes getPortfolioRes = portfolioService.onePortfolio(user, portfolio_id);

        return getPortfolioRes;
    }

}
