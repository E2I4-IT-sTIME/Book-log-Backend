package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostMeetingReq;
import com.dormammu.BooklogWeb.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final UserRepository userRepository;

    @PostMapping("/auth/meeting")
    public String createMeeting(@RequestBody PostMeetingReq postMeetingReq, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (postMeetingReq.getUserId() == principalDetails.getUser().getId()){
            meetingService.createMeeting(principalDetails.getUser(), postMeetingReq);
            return "모임 생성 완료";

        }
        return "안생겻음";
    }
}
