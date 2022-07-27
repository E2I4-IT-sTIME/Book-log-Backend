package com.dormammu.BooklogWeb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentRes {
    private int comment_id;
    private String content;
    private String username;
    private String email;
}
