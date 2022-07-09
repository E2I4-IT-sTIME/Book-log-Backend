package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchMeetingReq {

    private String name;
    private String info;
    private String ment;
    private String image;

    private boolean onoff;

    private int max_num;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
