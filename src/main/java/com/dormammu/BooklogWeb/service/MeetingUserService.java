package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUser;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUserRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostMeetingReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MeetingUserService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;


    @Transactional
    public String createMeeting(User user, Meeting meeting){
        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);

        meetingUserRepository.save(meetingUser);

        return "모임-유저 생성 완료";
    }
}
