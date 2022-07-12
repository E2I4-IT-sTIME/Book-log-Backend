package com.dormammu.BooklogWeb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommunityRes {
    private String title;
    private String img;
    private String writer;
    private String publisher;
    private String nickname;
}
