package com.dormammu.BooklogWeb.dto;

import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentReq {

    @ApiModelProperty(example = "댓글 내용")
    private String content;

    public Comment toEntity(User user, Meeting meeting){
        return Comment.builder()
        .user(user)
        .content(content)
        .meeting(meeting)
        .build();
    }
}
