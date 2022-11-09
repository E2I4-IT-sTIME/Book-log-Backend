package com.dormammu.BooklogWeb.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetNoticeRes {
    private Timestamp createDate;
    private String notice;
    private List<GetCommentRes> getCommentResList;
}
