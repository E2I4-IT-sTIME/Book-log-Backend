package com.dormammu.BooklogWeb.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentRes {
    private Timestamp createDate;
    private int comment_id;
    private String content;
    private String username;
    private String email;
}
