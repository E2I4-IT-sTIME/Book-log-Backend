package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.dto.PostCommentReq;
import com.dormammu.BooklogWeb.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"댓글 API"})  // Swagger 최상단 Controller 명칭
public class CommentController {

    private final CommentService commentService;

    @ApiOperation(value = "댓글 생성", notes = "댓글 생성 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @PostMapping("/auth/meeting/{meeting_id}/comment")  // 댓글 생성 api
    public String createComment(@PathVariable int meeting_id, Authentication authentication, @RequestBody PostCommentReq postCommentReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        commentService.createComment(meeting_id, principalDetails.getUser(), postCommentReq);
        return "댓글 생성 완료";
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글 수정 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @PatchMapping("/auth/meeting/{meeting_id}/comment/{comment_id}")  // 댓글 수정 api
    public String updateComment(@PathVariable int meeting_id, @PathVariable int comment_id, Authentication authentication, @RequestBody PostCommentReq postCommentReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        commentService.updateComment(principalDetails.getUser(), comment_id, postCommentReq);
        return "댓글 수정 완료";
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 api")
    @ApiImplicitParams(
                { @ApiImplicitParam(name = "meeting_id", value = "모임 id값"),
                        @ApiImplicitParam(name = "comment_id", value = "댓글 id값")
                }
            )
    @DeleteMapping("/auth/meeting/{meeting_id}/comment/{comment_id}")  // 댓글 삭제 api
    public String deleteComment(@PathVariable int meeting_id, @PathVariable int comment_id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        commentService.deleteComment(principalDetails.getUser(), comment_id);
        return "댓글 삭제 완료";
    }
}
