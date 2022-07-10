package com.dormammu.BooklogWeb.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRes {

    private String Q1;
    private String Q2;
    private String Q3;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
