package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostMeetingReq {
    private String name;
    private String info;
    private String ment;
    private String image;

//    private String q1;
//    private String q2;
//    private String q3;
//    private String q4;
//    private String q5;

    List<String> questions;

    private int max_num;

//    private String h1;
//    private String h2;
//    private String h3;
//    private String h4;
//    private String h5;
    List<String> hashtags;

    private Boolean onoff;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
