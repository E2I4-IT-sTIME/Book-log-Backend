package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.QnA.AdminQnARepository;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import com.dormammu.BooklogWeb.domain.hastag.HashTagRepository;
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
    private final AdminQnARepository adminQnARepository;
    private final HashTagRepository hashTagRepository;

    @Transactional
    public String createMeeting(User user, PostMeetingReq postMeetingReq){
        Meeting meeting = new Meeting();
        meeting.setName(postMeetingReq.getName());
        meeting.setInfo(postMeetingReq.getInfo());
        meeting.setMent(postMeetingReq.getMent());
        meeting.setImage(postMeetingReq.getImage());
        meeting.setUserId(user.getId());
        meeting.setMax_num(postMeetingReq.getMax_num());

        AdminQnA adminQnA = new AdminQnA();
        adminQnA.setMeeting(meeting);
        adminQnA.setQ1(postMeetingReq.getQ1());
        adminQnA.setQ2(postMeetingReq.getQ2());
        adminQnA.setQ3(postMeetingReq.getQ3());

        HashTag hashTag = new HashTag();
        hashTag.setMeeting(meeting);
        hashTag.setTag1(postMeetingReq.getH1());
        hashTag.setTag2(postMeetingReq.getH2());
        hashTag.setTag3(postMeetingReq.getH3());
        hashTag.setTag4(postMeetingReq.getH4());
        hashTag.setTag5(postMeetingReq.getH5());

        adminQnARepository.save(adminQnA);
        hashTagRepository.save(hashTag);
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
