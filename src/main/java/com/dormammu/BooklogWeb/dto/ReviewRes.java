package com.dormammu.BooklogWeb.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRes {
    private String title;
    private String content;
    private String book_name;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;
}
