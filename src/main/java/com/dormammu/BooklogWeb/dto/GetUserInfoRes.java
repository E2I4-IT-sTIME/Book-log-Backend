package com.dormammu.BooklogWeb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserInfoRes {
    private String name;
    private String profile;
}

