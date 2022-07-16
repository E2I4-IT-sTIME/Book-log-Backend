package com.dormammu.BooklogWeb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostNoticeReq {
    private String notice;

//    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    private LocalDate createDate;

}
