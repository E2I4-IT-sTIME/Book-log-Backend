package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUser;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUserRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
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

    @Transactional
    public String addMeeting(User user, Meeting meeting){
        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setUser(user);
        meetingUser.setMeeting(meeting);

        meetingUserRepository.save(meetingUser);
        return "모임-유저 입장 완료";
    }

    @Transactional
    public String outMeeting(User user, Meeting meeting){

        MeetingUser meetingUser =  meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting.getId());
        System.out.println(user.getId() +" AND " + meeting.getId());
        meetingUserRepository.delete(meetingUser);
        return "모임-유저 탈퇴 완료";
    }
}
