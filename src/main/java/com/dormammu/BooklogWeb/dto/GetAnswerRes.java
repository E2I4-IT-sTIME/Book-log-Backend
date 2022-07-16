package com.dormammu.BooklogWeb.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAnswerRes {
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;

    private int userId;
}
