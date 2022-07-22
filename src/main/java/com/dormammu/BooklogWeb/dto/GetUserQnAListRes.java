package com.dormammu.BooklogWeb.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserQnAListRes {
    private String username;
    private String email;

    private List<String> answers;

    private List<String> questions;
}
