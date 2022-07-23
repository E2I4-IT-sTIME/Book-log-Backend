package com.dormammu.BooklogWeb.dto;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRes {

    private String name;
    List<String> questions;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
