package com.dormammu.BooklogWeb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCommentRes {

    @ApiModelProperty(example = "댓글 작성 시간")
    private Timestamp createDate;

    @ApiModelProperty(example = "댓글 id값")
    private int comment_id;

    @ApiModelProperty(example = "댓글 내용")
    private String content;

    @ApiModelProperty(example = "작성자 이름")
    private String username;

    @ApiModelProperty(example = "작성자 이메일")
    private String email;
}
