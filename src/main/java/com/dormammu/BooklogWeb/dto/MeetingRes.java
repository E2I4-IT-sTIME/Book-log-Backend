package com.dormammu.BooklogWeb.dto;


import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(example = "모임 이름")
    private String name;

    @ApiModelProperty(example = "모임 질문 리스트")
    List<String> questions;

    @ApiModelProperty(example = "모임 생성 날짜")
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
