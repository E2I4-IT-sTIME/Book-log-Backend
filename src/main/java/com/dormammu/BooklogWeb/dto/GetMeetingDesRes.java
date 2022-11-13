package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMeetingDesRes {
    @ApiModelProperty(example = "유저 이미지 리스트")  // Swagger에 해당 필드가 무엇인지 나타냄
    private List<String> userImage;

    @ApiModelProperty(example = "모임 공지, 댓글")
    private GetNoticeRes getNoticeRes;  // 공지, 댓글

    @ApiModelProperty(example = "모임 이름")
    private String name;  // 모임 이름

    @ApiModelProperty(example = "모임 소개글")
    private String info;

    @ApiModelProperty(example = "온/오프라인")
    private boolean onoff;

    @ApiModelProperty(example = "현재 인원")
    private int cur_num;

    @ApiModelProperty(example = "모임 해시태그")
    private List<String> tagList;  // 모임 해시태그



}
