package com.dormammu.BooklogWeb.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserRes {

    @ApiModelProperty(example = "유저 id")
    private int id;

    @ApiModelProperty(example = "유저 이미지")
    private String image;

    @ApiModelProperty(example = "유저 이름")
    private String username;

    @ApiModelProperty(example = "유저 이메일")
    private String email;

}
