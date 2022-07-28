package com.dormammu.BooklogWeb.dto;


import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserRes {

    private int id;
    private String image;
    private String username;
    private String email;
    private Date birthday;
    private String job;
}
