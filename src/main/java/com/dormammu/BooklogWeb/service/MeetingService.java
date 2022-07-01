package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.PostMeetingReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingUserService meetingUserService;

    @Transactional
    public String createMeeting(User user, PostMeetingReq postMeetingReq){
        Meeting meeting = new Meeting();
        meeting.setName(postMeetingReq.getName());
        meeting.setUserId(user.getId());
        meeting.setMax_num(postMeetingReq.getMax_num());
        meeting.setMeeting_date(postMeetingReq.getMeeting_date());
        meetingRepository.save(meeting);
        meetingUserService.createMeeting(user, meeting);

        return "모임 생성 완료";
    }

    @Transactional
    public List<Meeting> meetingList(){
        System.out.println("meetingList 들어옴");
        List<Meeting> meetingList = meetingRepository.findAll();
        System.out.println("모임 리스트 출력 : " + meetingList);
        return meetingList;
    }

    @Transactional(readOnly = true)
    public List<Meeting> myMeetingList(User user){
        System.out.println("myMeetingList 들어옴");
        List<Meeting> myMeetingList = meetingRepository.findByUserId(user.getId());
        System.out.println("내 모임 리스트 출력: " + myMeetingList);
        return myMeetingList;
    }

}
