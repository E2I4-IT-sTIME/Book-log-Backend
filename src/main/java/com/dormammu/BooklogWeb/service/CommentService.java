package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.comment.CommentRepository;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.dto.PostCommentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public String createComment(int meeting_id, User user, PostCommentReq postCommentReq){
        Meeting meeting = meetingRepository.findById(meeting_id);
        Comment comment = postCommentReq.toEntity(user, meeting);
        commentRepository.save(comment);

        return "댓글 작성 완료";
    }
}
