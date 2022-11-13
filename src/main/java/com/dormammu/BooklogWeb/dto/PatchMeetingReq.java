package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchMeetingReq {
    @ApiModelProperty(example = "모임 이름")
    private String name;

    @ApiModelProperty(example = "모임 소개글")
    private String info;

    @ApiModelProperty(example = "모임 가입 멘트")
    private String ment;

    @ApiModelProperty(example = "모임 이미지")
    private String image;

    @ApiModelProperty(example = "온/오프라인")
    private boolean onoff;

    @ApiModelProperty(example = "최대인원")
    private int max_num;

    @ApiModelProperty(example = "모임 생성 날짜")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
