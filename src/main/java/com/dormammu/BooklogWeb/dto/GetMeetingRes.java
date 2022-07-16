package com.dormammu.BooklogWeb.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMeetingRes {
    private String info;
    private String image;
    private String ment;
    private String name;

    private int max_num;
    private int cur_num;

    private boolean onoff;

    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;

    private String q1;
    private String q2;
    private String q3;
    private String q4;
    private String q5;
}
