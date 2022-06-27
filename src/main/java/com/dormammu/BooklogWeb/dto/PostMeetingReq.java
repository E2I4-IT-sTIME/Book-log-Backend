package com.dormammu.BooklogWeb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMeetingReq {

    private String name;

    private boolean onoff;

    private int userId;

    private int max_num;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date meeting_date;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
