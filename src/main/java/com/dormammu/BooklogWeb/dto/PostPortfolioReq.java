package com.dormammu.BooklogWeb.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPortfolioReq {
    private String title;

    //private int userId;

    private String content;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;
}
