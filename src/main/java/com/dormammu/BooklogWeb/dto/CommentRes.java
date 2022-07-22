package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRes {
    private String content;
    private String username;
}
