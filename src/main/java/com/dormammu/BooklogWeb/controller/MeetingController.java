package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import com.dormammu.BooklogWeb.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

    @PostMapping("/auth/meeting")  // 모임 생성 API
    public String createMeeting(@RequestBody PostMeetingReq postMeetingReq, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());
        if (user.getId() == principalDetails.getUser().getId()){
            meetingService.createMeeting(principalDetails.getUser(), postMeetingReq);
            return "모임 생성 완료";

        }
        return null;
    }

    @GetMapping("/meetings")  // 모임 리스트 조회 API
    public List<Meeting> meetingList(){
        System.out.println("controller로 들어옴");
        return meetingService.meetingList();
    }

    @GetMapping("/api/user/{id}/meetings")  // 내 모임 조회 API
    public List<Meeting> myMeetingList(@PathVariable int id, Authentication authentication){

        User user = userRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (user.getId() == principalDetails.getUser().getId()){
            System.out.println("현재 로그인된 유저 : " + principalDetails.getUser().getUsername());
            return meetingService.myMeetingList(user);
        }
        return null;
    }

    @GetMapping("/auth/meetings/{id}")  // 모임 입장 API
    public String addMeeting(@PathVariable int id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.addMeeting(user, meeting);
        }

        return null;
    }

    @DeleteMapping("/auth/meeting/{id}/out")  // 모임 탈퇴 API
    public String outMeeting(@PathVariable int id, Authentication authentication){
        Meeting meeting = meetingRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.outMeeting(user, meeting);
        }
        return null;
    }

    @PatchMapping("/auth/meeting/{id}")  // 모임 수정 API
    public String updateMeeting(@PathVariable int id, Authentication authentication, @RequestBody PatchMeetingReq patchMeetingReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(meeting.getUserId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.update(id, patchMeetingReq);  // 모임 id 넘겨줌
        }
        return null;
    }

    @DeleteMapping("/auth/meeting/{id}")  // 모임 삭제 API
    public String deleteMeeting(@PathVariable int id, Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if(user.getId() == principalDetails.getUser().getId()){
            return meetingService.deleteMeeting(user, meeting);
        }
        return null;
    }

    @GetMapping("/auth/{id}/question")  // 모임 질문 조회 API
    public MeetingRes questionList(@PathVariable int id, Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.questionList(meeting);
        }
        return null;
    }

    @PostMapping("/auth/{id}/answer")  // 모임 답변 생성 API
    public String createAnswer(@RequestBody PostAnswerReq postAnswerReq, @PathVariable int id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()) {
           return meetingService.createAnswer(principalDetails.getUser(), id, postAnswerReq);
        }
        return null;
    }

    @GetMapping("/auth/meetings/{meeting_id}/answers")  // 모임 답변 전체 조회 api
    public List<GetUserQnAListRes> answerList(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(meeting_id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){  // 로그인한 사용자 == 접근한 사용자
            // 접근한 사람이 모임 관리자인지 확인
            if (user.getId() == meeting.getUserId()) {
                return meetingService.answerList(meeting_id);
            }
            return null;
        }
        return null;
    }

    @GetMapping("/auth/meetings/{meeting_id}/answers/{answer_id}")  // 모임 답변 개별 조회 api
    public GetAnswerRes oneAnswer(@PathVariable int meeting_id, @PathVariable int answer_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.oneAnswer(user,meeting_id);
        }
        return null;
    }

    @DeleteMapping("/auth/{meeting_id}/answer/{answer_id}")  // 모임 답변 삭제 api
    public String deleteAnswer(@PathVariable int meeting_id, @PathVariable int answer_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.deleteAnswer(user, meeting_id, answer_id);
        }
        return null;
    }

    @DeleteMapping("/auth/meeting/{meeting_id}/out/{user_id}")  // 모임 강퇴 api
    public String outUser(@PathVariable int meeting_id, @PathVariable int user_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.outUser(user, meeting_id, user_id);
        }
        return null;
    }
}