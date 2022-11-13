package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(example = "모임 이름")
    private String name;

    @ApiModelProperty(example = "모임 소개글")
    private String info;

    @ApiModelProperty(example = "모임 가입 멘트")
    private String ment;

    @ApiModelProperty(example = "모임 이미지")
    private String image;

//    private String q1;
//    private String q2;
//    private String q3;
//    private String q4;
//    private String q5;

    @ApiModelProperty(example = "모임 질문 리스트")
    List<String> questions;

    @ApiModelProperty(example = "최대 인원")
    private int max_num;

//    private String h1;
//    private String h2;
//    private String h3;
//    private String h4;
//    private String h5;
    @ApiModelProperty(example = "모임 해시태그 리스트")
    List<String> hashtags;

    @ApiModelProperty(example = "온/오프라인")
    private Boolean onoff;

    @ApiModelProperty(example = "모임 생성 날짜")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
