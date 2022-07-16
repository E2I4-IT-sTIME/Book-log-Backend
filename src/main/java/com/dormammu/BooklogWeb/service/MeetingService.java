package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.QnA.AdminQnARepository;
import com.dormammu.BooklogWeb.domain.QnA.UserQnA;
import com.dormammu.BooklogWeb.domain.QnA.UserQnARepository;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import com.dormammu.BooklogWeb.domain.hastag.HashTagRepository;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUser;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUserRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final AdminQnARepository adminQnARepository;
    private final HashTagRepository hashTagRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final UserQnARepository userQnARepository;

    @Transactional
    public String createMeeting(User user, PostMeetingReq postMeetingReq){
        Meeting meeting = new Meeting();
        meeting.setName(postMeetingReq.getName());
        meeting.setInfo(postMeetingReq.getInfo());
        meeting.setMent(postMeetingReq.getMent());
        meeting.setImage(postMeetingReq.getImage());
        meeting.setUserId(user.getId());
        meeting.setCur_num(1);
        meeting.setMax_num(postMeetingReq.getMax_num());

        AdminQnA adminQnA = new AdminQnA();
        adminQnA.setMeeting(meeting);
        adminQnA.setQ1(postMeetingReq.getQ1());
        adminQnA.setQ2(postMeetingReq.getQ2());
        adminQnA.setQ3(postMeetingReq.getQ3());
        adminQnA.setQ4(postMeetingReq.getQ4());
        adminQnA.setQ5(postMeetingReq.getQ5());
//        meeting.setAdminQnA(adminQnA);


        HashTag hashTag = new HashTag();
        hashTag.setMeeting(meeting);
        hashTag.setTag1(postMeetingReq.getH1());
        hashTag.setTag2(postMeetingReq.getH2());
        hashTag.setTag3(postMeetingReq.getH3());
        hashTag.setTag4(postMeetingReq.getH4());
        hashTag.setTag5(postMeetingReq.getH5());
//        meeting.setHashTag(hashTag);

        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);
        adminQnARepository.save(adminQnA);
        hashTagRepository.save(hashTag);
        meetingRepository.save(meeting);
        AdminQnA adminQnA1 = adminQnARepository.findById(adminQnA.getId());
        HashTag hashTag1 = hashTagRepository.findById(hashTag.getId());

        meeting.setHashTag(hashTag1);
        meeting.setAdminQnA(adminQnA1);

        return "모임 생성 완료";
    }

    @Transactional
    public List<GetMeetingRes> meetingList(){
        System.out.println("meetingList 들어옴");
        List<Meeting> meetingList = meetingRepository.findAll();
        List<GetMeetingRes> meetingResList = new ArrayList<>();

        for (Meeting mt: meetingList){
            HashTag hashTag = hashTagRepository.findById(mt.getHashTag().getId());
            AdminQnA adminQnA = adminQnARepository.findById(mt.getAdminQnA().getId());
            GetMeetingRes getMeetingRes = GetMeetingRes.builder()
                    .info(mt.getInfo())
                    .image(mt.getImage())
                    .ment(mt.getMent())
                    .name(mt.getName())
                    .max_num(mt.getMax_num())
                    .cur_num(mt.getCur_num())
                    .onoff(mt.isOnoff())
                    .a1(hashTag.getTag1())
                    .a2(hashTag.getTag2())
                    .a3(hashTag.getTag3())
                    .a4(hashTag.getTag4())
                    .a5(hashTag.getTag5())
                    .q1(adminQnA.getQ1())
                    .q2(adminQnA.getQ2())
                    .q3(adminQnA.getQ3())
                    .q4(adminQnA.getQ4())
                    .q5(adminQnA.getQ5()).build();

            meetingResList.add(getMeetingRes);
        }
        return meetingResList;
    }

    @Transactional(readOnly = true)
    public List<GetMeetingRes> myMeetingList(User user){
        System.out.println("myMeetingList 들어옴");
        List<Meeting> myMeetings = meetingRepository.findByUserId(user.getId());
//        System.out.println("내 모임: " + myMeetings);
        List<GetMeetingRes> myMeetingList = new ArrayList<>();

        for (Meeting mt: myMeetings){
            HashTag hashTag = hashTagRepository.findById(mt.getHashTag().getId());
            AdminQnA adminQnA = adminQnARepository.findById(mt.getAdminQnA().getId());
            GetMeetingRes getMeetingRes = GetMeetingRes.builder()
                    .info(mt.getInfo())
                    .image(mt.getImage())
                    .ment(mt.getMent())
                    .name(mt.getName())
                    .max_num(mt.getMax_num())
                    .cur_num(mt.getCur_num())
                    .onoff(mt.isOnoff())
                    .a1(hashTag.getTag1())
                    .a2(hashTag.getTag2())
                    .a3(hashTag.getTag3())
                    .a4(hashTag.getTag4())
                    .a5(hashTag.getTag5())
                    .q1(adminQnA.getQ1())
                    .q2(adminQnA.getQ2())
                    .q3(adminQnA.getQ3())
                    .q4(adminQnA.getQ4())
                    .q5(adminQnA.getQ5()).build();
            myMeetingList.add(getMeetingRes);
        }

        return myMeetingList;
    }

    @Transactional
    public String addMeeting(User user, Meeting meeting){
        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setUser(user);
        meetingUser.setMeeting(meeting);
        meeting.setCur_num(meeting.getCur_num()+1);  // 인원 추가

        meetingUserRepository.save(meetingUser);
        return "모임 입장 완료";
    }

    @Transactional
    public String outMeeting(User user, Meeting meeting){

        MeetingUser meetingUser =  meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting.getId());
        System.out.println(user.getId() +" AND " + meeting.getId());
        meeting.setCur_num(meeting.getCur_num()-1);
        meetingUserRepository.delete(meetingUser);

        return "모임 탈퇴 완료";
    }

    @Transactional
    public String update(int id, PatchMeetingReq patchMeetingReq){
        Meeting meeting = meetingRepository.findById(id);
//        System.out.println(meeting.getName() + "의 모임 정보를 수정합니다");
        meeting.setName(patchMeetingReq.getName());
        meeting.setInfo(patchMeetingReq.getInfo());
        meeting.setMent(patchMeetingReq.getMent());
        meeting.setImage(patchMeetingReq.getImage());
        meeting.setOnoff(patchMeetingReq.isOnoff());
        meeting.setMax_num(patchMeetingReq.getMax_num());

        return "모임 정보 수정 완료";
    }

    @Transactional
    public String deleteMeeting(User user, Meeting meeting){
        System.out.println("User :" + user.getUsername() + ", " + "Meeting : " + meeting.getName());
        meetingRepository.delete(meeting);
        System.out.println("모임 삭제됨");

//        System.out.println("큐앤에이, 해시태그, 미팅유저 삭제됨");  // 모임 삭제 -> meetingUser전부, 해시태그, 큐앤에이
        return "모임 삭제 완료";
    }

    @Transactional(readOnly = true)
    public MeetingRes questionList(Meeting meeting){
        // 미팅을 가져와서
        AdminQnA question = adminQnARepository.findByMeetingId(meeting.getId());
        List<String> questions = new ArrayList<>();

        questions.add(question.getQ1());
        questions.add(question.getQ2());
        questions.add(question.getQ3());
        questions.add(question.getQ4());
        questions.add(question.getQ5());
//        System.out.println("질문 리스트 출력 : " + questions);

        MeetingRes meetingRes = MeetingRes.builder()
                .Q1(question.getQ1())
                .Q2(question.getQ2())
                .Q3(question.getQ3())
                .Q4(question.getQ4())
                .Q5(question.getQ5()).build();
        return meetingRes;
    }

    @Transactional
    public String createAnswer(User user, int id, PostAnswerReq postAnswerReq){
        UserQnA userQnA = new UserQnA();

        userQnA.setA1(postAnswerReq.getA1());
        userQnA.setA2(postAnswerReq.getA2());
        userQnA.setA3(postAnswerReq.getA3());
        userQnA.setA4(postAnswerReq.getA4());
        userQnA.setA5(postAnswerReq.getA5());
        userQnA.setUserId(user.getId());

        // 모임 id
        AdminQnA adminQnA = adminQnARepository.findByMeetingId(id);
        userQnA.setAdminQnA(adminQnA);
        userQnARepository.save(userQnA);
        return "답변 생성 완료";
    }

    @Transactional(readOnly = true)
    public List<GetUserQnAListRes> answerList(int meeting_id){
        Meeting meeting = meetingRepository.findById(meeting_id);

        List<UserQnA> userQnAList =  userQnARepository.findByAdminQnAId(meeting.getAdminQnA().getId());
        List<GetUserQnAListRes> getUserQnAListResList =  new ArrayList<>();

        for (UserQnA userqna : userQnAList){
            GetUserQnAListRes getUserQnAListRes = new GetUserQnAListRes();
            getUserQnAListRes.setUserId(userqna.getUserId());
            getUserQnAListRes.setA1(userqna.getA1());
            getUserQnAListRes.setA2(userqna.getA2());
            getUserQnAListRes.setA3(userqna.getA3());
            getUserQnAListRes.setA4(userqna.getA4());
            getUserQnAListRes.setA5(userqna.getA5());

            getUserQnAListResList.add(getUserQnAListRes);
        }
        System.out.println("answerList 들어옴");
        return getUserQnAListResList;
    }

    @Transactional(readOnly = true)
    public GetAnswerRes oneAnswer(User user, int meeting_id){
        // user랑 meeting_id 넘겨받아서
        AdminQnA adminQnA = adminQnARepository.findByMeetingId(meeting_id);
        UserQnA userQnA = userQnARepository.findByUserIdAndAdminQnAId(user.getId(), adminQnA.getId());

        GetAnswerRes getMeetingRes = new GetAnswerRes();
        getMeetingRes.setA1(userQnA.getA1());
        getMeetingRes.setA2(userQnA.getA2());
        getMeetingRes.setA3(userQnA.getA3());
        getMeetingRes.setA4(userQnA.getA4());
        getMeetingRes.setA5(userQnA.getA5());
        getMeetingRes.setUserId(user.getId());

        return getMeetingRes;
    }

    @Transactional
    public String deleteAnswer(User user, int meeting_id, int answer_id){
        UserQnA userQnA = userQnARepository.findById(answer_id);
        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()){ // 삭제하려는 사람이 모임 만든사람인지 확인
            UserQnA userQnA1 = userQnARepository.findByUserIdAndAdminQnAId(userQnA.getId(), userQnA.getAdminQnA().getId());
            userQnARepository.delete(userQnA1);
            return "모임 답변 삭제 완료";
        }
        return null;
    }

    @Transactional
    public String outUser(User user, int meeting_id, int user_id){

        Meeting meeting = meetingRepository.findById(meeting_id);
        // 현재인원에서 한명 빠지고 , meeting-user에서 삭제됨
        if (meeting.getUserId() == user.getId()) {  // 모임 만든사람만 삭제권한이 있으므로 확인해주기
            meeting.setCur_num(meeting.getCur_num() - 1); // 인원수 -1

            MeetingUser meetingUser = meetingUserRepository.findByUserIdAndMeetingId(user_id, meeting_id);
            meetingUserRepository.delete(meetingUser);
            return "모임 강퇴 완료";
        }
        return null;
    }
}