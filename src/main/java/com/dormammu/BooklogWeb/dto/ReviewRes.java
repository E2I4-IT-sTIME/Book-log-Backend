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
public class ReviewRes {
    private String title;
    private String content;
    private String book_name;
    private int review_id;

//    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    private LocalDate createDate;

    @CreationTimestamp
    private Timestamp createDate;
}
