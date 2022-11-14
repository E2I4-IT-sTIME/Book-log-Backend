package com.dormammu.BooklogWeb.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserQnAListRes {
    @ApiModelProperty(example = "유저 이름")
    private String username;

    @ApiModelProperty(example = "유저 이메일")
    private String email;

    @ApiModelProperty(example = "답변 리스트")
    private List<String> answers;

    @ApiModelProperty(example = "질문 리스트")
    private List<String> questions;
}
