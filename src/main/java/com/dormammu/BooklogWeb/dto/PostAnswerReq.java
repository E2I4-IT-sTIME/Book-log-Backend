package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostAnswerReq {
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;

    List<String> answers;

}
