package com.dormammu.BooklogWeb.dto;


import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMeetingRes {
    @ApiModelProperty(example = "모임 id값")
    private int id; // meeting_id

    @ApiModelProperty(example = "isbn정보(string으로)")
    private String isbn;  // 책 정보

    @ApiModelProperty(example = "모임 이름")
    private String name;  // 모임 이름

    @ApiModelProperty(example = "모임 이미지")
    private String image;

    @ApiModelProperty(example = "온/오프라인")
    private boolean onoff;  // 온라인, 오프라인

    @ApiModelProperty(example = "모임 개설자 이름")
    private String username;

    @ApiModelProperty(example = "최대 인원")
    private int max_num;

    @ApiModelProperty(example = "현재 인원")
    private int cur_num;

    @ApiModelProperty(example = "해시태그")
    List<String> tags;

    @ApiModelProperty(example = "모임 소개글")
    private String info;
}
