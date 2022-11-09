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
//    private int id; // meeting_id
//    private String info;
//    private String image;
//    private String name;
//    private int max_num;
//    private int cur_num;
//    private boolean onoff;
//    private String username;
//    private String email;
//
//    List<String> tags;


    private int id; // meeting_id
    private String isbn;  // 책 정보
    private String name;  // 모임 이름
    private String image;
    private boolean onoff;  // 온라인, 오프라인
    private String username;
    private int max_num;
    private int cur_num;
    List<String> tags;
}
