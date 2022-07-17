package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.user.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentReq {

    private String content;

    public Comment toEntity(User user, Meeting meeting){
        return Comment.builder()
        .user(user)
        .content(content)
        .meeting(meeting)
        .build();
    }
}
