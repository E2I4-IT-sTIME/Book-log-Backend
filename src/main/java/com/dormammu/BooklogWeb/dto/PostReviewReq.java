package com.dormammu.BooklogWeb.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostReviewReq {

    //private int portfolio_id;

    private String title;

    private String content;

    private String book_name;

//    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    private LocalDate createDate;
    @CreationTimestamp
    private Timestamp createDate;
}
