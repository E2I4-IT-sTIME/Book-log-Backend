package com.dormammu.BooklogWeb.dto;

import lombok.*;
import org.springframework.lang.Nullable;

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
    private String username;

    @Nullable
    private String content;
}
