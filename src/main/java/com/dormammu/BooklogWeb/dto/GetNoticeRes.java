package com.dormammu.BooklogWeb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetNoticeRes {
    private String notice;
    private List<GetCommentRes> getCommentResList;
}
