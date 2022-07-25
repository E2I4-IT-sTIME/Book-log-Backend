package com.dormammu.BooklogWeb.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOneMeetingRes {
    private int id; // meeting_id
    private String info;
    private String ment;
    private int max_num;
    private int cur_num;
    private String image;
    private String name;
    private String email;
    private String notice;
    private boolean onoff;

    List<String> tags;
}
