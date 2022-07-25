package com.dormammu.BooklogWeb.dto;


import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMeetingRes {
    private int id; // meeting_id
    private String info;
    private String image;
    private String name;
    private int max_num;
    private int cur_num;
    private boolean onoff;
    private String username;
    private String email;

    List<String> tags;
}
