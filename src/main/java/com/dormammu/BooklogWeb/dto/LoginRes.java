package com.dormammu.BooklogWeb.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRes {
    private String jwtToken;
    private Boolean isExist;
    private int userId;
}
