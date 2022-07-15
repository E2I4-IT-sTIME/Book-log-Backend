package com.dormammu.BooklogWeb.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserQnAListRes {
    private int userId;  // 답변자 id
    private String A1;  // 질문1에 대한 답변1
    private String A2;  // 질문2에 대한 답변2
    private String A3;
    private String A4;
    private String A5;
}
