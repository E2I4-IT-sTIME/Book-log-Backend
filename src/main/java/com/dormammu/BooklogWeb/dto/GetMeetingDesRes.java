package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMeetingDesRes {
    private List<String> userImage;
    private GetNoticeRes getNoticeRes;  // 공지, 댓글
    private String name;  // 모임 이름
    private String info;
    private boolean onoff;
    private int cur_num;
    private List<String> tagList;  // 모임 해시태그



}
