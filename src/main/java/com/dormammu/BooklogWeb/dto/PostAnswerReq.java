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

    List<String> answers;

}
