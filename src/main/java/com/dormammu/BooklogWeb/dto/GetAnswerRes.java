package com.dormammu.BooklogWeb.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAnswerRes {

    private int qna_id;
    private int user_id;
    private String username;
    private String email;

    private List<String> answers;
    private List<String> questions;
}
