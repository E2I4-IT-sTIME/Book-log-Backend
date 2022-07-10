package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostAnswerReq {
    private int userId;
    private String a1;
    private String a2;
    private String a3;
}
