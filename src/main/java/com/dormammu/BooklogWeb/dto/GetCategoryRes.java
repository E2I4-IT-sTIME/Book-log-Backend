package com.dormammu.BooklogWeb.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCategoryRes {
    @ApiModelProperty(example = "모임 id값")
    private int id; // meeting_id

    @ApiModelProperty(example = "모임 이름")
    private String name;  // 모임 이름

    @ApiModelProperty(example = "모임 이미지")
    private String image;

    @ApiModelProperty(example = "온/오프라인")
    private boolean onoff;  // 온라인, 오프라인

    @ApiModelProperty(example = "최대 인원")
    private int max_num;

    @ApiModelProperty(example = "현재 인원")
    private int cur_num;

    @ApiModelProperty(example = "모임 소개글")
    private String info;

    @ApiModelProperty(example = "해시태그 id")
    private int hashTag_id;
}
