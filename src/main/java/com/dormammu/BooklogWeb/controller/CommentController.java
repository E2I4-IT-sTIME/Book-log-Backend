package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.dto.CommentListRes;
import com.dormammu.BooklogWeb.dto.CommentRes;
import com.dormammu.BooklogWeb.dto.PostCommentReq;
import com.dormammu.BooklogWeb.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/auth/meeting/{meeting_id}/comment")  // 댓글 생성 api
    public String createComment(@PathVariable int meeting_id, Authentication authentication, @RequestBody PostCommentReq postCommentReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        commentService.createComment(meeting_id, principalDetails.getUser(), postCommentReq);
        return "댓글 생성 완료";
    }

    @PatchMapping("/auth/meeting/{meeting_id}/comment/{comment_id}")  // 댓글 수정 api
    public String updateComment(@PathVariable int meeting_id, @PathVariable int comment_id, Authentication authentication, @RequestBody PostCommentReq postCommentReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        commentService.updateComment(principalDetails.getUser(), comment_id, postCommentReq);
        return "댓글 수정 완료";
    }

    @DeleteMapping("/auth/meeting/{meeting_id}/comment/{comment_id}")  // 댓글 삭제 api
    public String deleteComment(@PathVariable int meeting_id, @PathVariable int comment_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        commentService.deleteComment(principalDetails.getUser(), comment_id);
        return "댓글 삭제 완료";
    }

    @GetMapping("/auth/meeting/{meeting_id}/comment")  // 공지 댓글 리스트 조회 API
    public CommentListRes commentList(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("정상 실행");
        return commentService.commentsList(meeting_id);
    }
}
