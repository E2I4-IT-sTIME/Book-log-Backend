package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.MeetingRes;
import com.dormammu.BooklogWeb.dto.PatchMeetingReq;
import com.dormammu.BooklogWeb.dto.PostMeetingReq;
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

    @PostMapping("/auth/meeting")
    public String createMeeting(@RequestBody PostMeetingReq postMeetingReq, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());
        if (user.getId() == principalDetails.getUser().getId()){
            meetingService.createMeeting(principalDetails.getUser(), postMeetingReq);
            return "모임 생성 완료";

        }
        return null;
    }

    @GetMapping("/meetings")
    public List<Meeting> meetingList(){
        System.out.println("controller로 들어옴");
        return meetingService.meetingList();
    }

    @GetMapping("/api/user/{id}/meetings")
    public List<Meeting> myMeetingList(@PathVariable int id, Authentication authentication){

        User user = userRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (user.getId() == principalDetails.getUser().getId()){
            System.out.println("현재 로그인된 유저 : " + principalDetails.getUser().getUsername());
            return meetingService.myMeetingList(user);
        }
        return null;
    }

    @GetMapping("/auth/meetings/{id}")
    public String addMeeting(@PathVariable int id, Authentication authentication){
        System.out.println("들어옴1");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.addMeeting(user, meeting);
        }

        return null;
    }

    @DeleteMapping("/auth/meeting/{id}/out")
    public String outMeeting(@PathVariable int id, Authentication authentication){
        Meeting meeting = meetingRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.outMeeting(user, meeting);
        }
        return null;
    }

    @PatchMapping("/auth/meeting/{id}")
    public String updateMeeting(@PathVariable int id, Authentication authentication, @RequestBody PatchMeetingReq patchMeetingReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(meeting.getUserId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.update(id, patchMeetingReq);  // 모임 id 넘겨줌
        }
        return null;
    }

    //    @DeleteMapping("/auth/meeting/{id}")  // 모임 삭제
//    public String deleteMeeting(@PathVariable int id, Authentication authentication){
//
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        Meeting meeting = meetingRepository.findById(id);
//        User user = userRepository.findById(principalDetails.getUser().getId());
//
//        if(user.getId() == principalDetails.getUser().getId()){
//            return meetingService.deleteMeeting(user, meeting);
//        }
//        return null;
//    }

    @PostMapping("/auth/{id}/question")
    public MeetingRes questionList(@PathVariable int id, Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.questionList(meeting);
        }
        return null;
    }
}