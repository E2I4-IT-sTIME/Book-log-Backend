package com.dormammu.BooklogWeb.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPortfolioRes {
    //private String nickname;
    private String title; // 포트폴리오 제목
    private String content; // 포트폴리오 설명
    private String image;

    @Nullable
    List<ReviewRes> reviewResList;

}
