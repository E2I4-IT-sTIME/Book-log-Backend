package com.dormammu.BooklogWeb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPortfolioRes {
    private int userId;
    private String nickname;
    private String title; // 포트폴리오 제목

    List<ReviewRes> reviewResList;

}
