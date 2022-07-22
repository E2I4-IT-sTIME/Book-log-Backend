package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.comment.CommentRepository;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.dto.CommentListRes;
import com.dormammu.BooklogWeb.dto.CommentRes;
import com.dormammu.BooklogWeb.dto.PostCommentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public String updateComment(User user, int comment_id, PostCommentReq postCommentReq){
        Comment comment = commentRepository.findById(comment_id);

        if (comment.getUser().getId() == user.getId()) {
            comment.setContent(postCommentReq.getContent());
            return "댓글 수정 완료";
        } else {
            return null;
        }
    }

    @Transactional
    public String deleteComment(User user, int comment_id){
        Comment comment = commentRepository.findById(comment_id);

        if (comment.getUser().getId() == user.getId()){
            commentRepository.delete(comment);
            return "댓글 삭제 완료";
        } else{
            return null;
        }
    }

    @Transactional(readOnly = true)
    public CommentListRes commentsList(int meeting_id){

        List<Comment> commentList = commentRepository.findByMeetingId(meeting_id);

        List<CommentRes> commentResList = new ArrayList<>();

        for (Comment cm: commentList){
            CommentRes commentRes = CommentRes.builder()
                    .content(cm.getContent())
                    .username(cm.getUser().getUsername())
                    .build();
            commentResList.add(commentRes);
//            System.out.println(cm.getContent());
        }

        CommentListRes commentListRes = CommentListRes.builder()
                .commentResList(commentResList).build();
        return commentListRes;
    }
}
