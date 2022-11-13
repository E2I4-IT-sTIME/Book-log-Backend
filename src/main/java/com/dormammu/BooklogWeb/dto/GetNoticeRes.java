package com.dormammu.BooklogWeb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetNoticeRes {
    @ApiModelProperty(example = "작성 시간")
    private Timestamp createDate;

    @ApiModelProperty(example = "공지 내용")
    private String notice;

    @ApiModelProperty(example = "공지에 대한 댓글 리스트")
    private List<GetCommentRes> getCommentResList;
}
