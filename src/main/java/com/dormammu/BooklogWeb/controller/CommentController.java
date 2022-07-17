package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.comment.CommentRepository;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostCommentReq;
import com.dormammu.BooklogWeb.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
