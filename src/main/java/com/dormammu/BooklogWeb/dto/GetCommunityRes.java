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
    private String content;
    private String book_name;
    private String username;
}
