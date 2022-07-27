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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Uploader s3Uploader;


//    @Transactional
//    public String createMeeting(User user, MultipartFile multipartFile, PostMeetingReq postMeetingReq) throws IOException {
//        //        return meetingService.createMeeting(user, multipartFile, name, info, ment, image, max_num, onoff, questions, hashtags);
//        Meeting meeting = new Meeting();
//        meeting.setName(postMeetingReq.getName());
//        meeting.setInfo(postMeetingReq.getInfo());
//        meeting.setMent(postMeetingReq.getMent());
////        meeting.setImage(postMeetingReq.getImage());
//        meeting.setUserId(user.getId());
//        meeting.setCur_num(1);
//        meeting.setMax_num(postMeetingReq.getMax_num());
//        meeting.setOnoff(postMeetingReq.getOnoff());
//
//        AdminQnA adminQnA = new AdminQnA();
//        adminQnA.setMeeting(meeting);
//        System.out.println(postMeetingReq.getQuestions());
//        adminQnA.setQ1(postMeetingReq.getQuestions().get(0));
//        adminQnA.setQ2(postMeetingReq.getQuestions().get(1));
//        adminQnA.setQ3(postMeetingReq.getQuestions().get(2));
//        adminQnA.setQ4(postMeetingReq.getQuestions().get(3));
//        adminQnA.setQ5(postMeetingReq.getQuestions().get(4));
//
////        adminQnA.setQ1(postMeetingReq.getQ1());
////        adminQnA.setQ2(postMeetingReq.getQ2());
////        adminQnA.setQ3(postMeetingReq.getQ3());
////        adminQnA.setQ4(postMeetingReq.getQ4());
////        adminQnA.setQ5(postMeetingReq.getQ5());
//
//
//        HashTag hashTag = new HashTag();
//        hashTag.setMeeting(meeting);
//        hashTag.setTag1(postMeetingReq.getHashtags().get(0));
//        hashTag.setTag2(postMeetingReq.getHashtags().get(1));
//        hashTag.setTag3(postMeetingReq.getHashtags().get(2));
//        hashTag.setTag4(postMeetingReq.getHashtags().get(3));
//        hashTag.setTag5(postMeetingReq.getHashtags().get(4));
//
//
////        hashTag.setTag1(postMeetingReq.getH1());
////        hashTag.setTag2(postMeetingReq.getH2());
////        hashTag.setTag3(postMeetingReq.getH3());
////        hashTag.setTag4(postMeetingReq.getH4());
////        hashTag.setTag5(postMeetingReq.getH5());
//
//
//        MeetingUser meetingUser = new MeetingUser();
//        meetingUser.setMeeting(meeting);
//        meetingUser.setStatus("모임장");
//        meetingUser.setUser(user);
//        meetingUserRepository.save(meetingUser);
//        adminQnARepository.save(adminQnA);
//        hashTagRepository.save(hashTag);
//        meetingRepository.save(meeting);
//        AdminQnA adminQnA1 = adminQnARepository.findById(adminQnA.getId());
//        HashTag hashTag1 = hashTagRepository.findById(hashTag.getId());
//
//        meeting.setHashTag(hashTag1);
//        meeting.setAdminQnA(adminQnA1);
//
//        String r = s3Uploader.upload(meeting.getId(), multipartFile, "static");
//        System.out.println(r);
//        return "모임 생성 완료";
//    }


    @Transactional
    public String createMeeting(User user, MultipartFile multipartFile, String name, String info, String ment, String max_num, String onoff, List<String> questions, List<String> hashtags) throws IOException {
        //        return meetingService.createMeeting(user, multipartFile, name, info, ment, image, max_num, onoff, questions, hashtags);
        Meeting meeting = new Meeting();
        meeting.setName(name);
        meeting.setInfo(info);
        meeting.setMent(ment);
//        meeting.setImage(postMeetingReq.getImage());
        meeting.setUserId(user.getId());
        meeting.setCur_num(1);
        meeting.setMax_num(Integer.parseInt(max_num));
        meeting.setOnoff(Boolean.parseBoolean(onoff));

        AdminQnA adminQnA = new AdminQnA();
        adminQnA.setMeeting(meeting);
//        System.out.println(postMeetingReq.getQuestions());
        adminQnA.setQ1(questions.get(0));
        adminQnA.setQ2(questions.get(1));
        adminQnA.setQ3(questions.get(2));
        adminQnA.setQ4(questions.get(3));
        adminQnA.setQ5(questions.get(4));

        HashTag hashTag = new HashTag();
        hashTag.setMeeting(meeting);
        hashTag.setTag1(hashtags.get(0));
        hashTag.setTag2(hashtags.get(1));
        hashTag.setTag3(hashtags.get(2));
        hashTag.setTag4(hashtags.get(3));
        hashTag.setTag5(hashtags.get(4));


        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setStatus("모임장");
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);
        adminQnARepository.save(adminQnA);
        hashTagRepository.save(hashTag);
        meetingRepository.save(meeting);
        AdminQnA adminQnA1 = adminQnARepository.findById(adminQnA.getId());
        HashTag hashTag1 = hashTagRepository.findById(hashTag.getId());

        meeting.setHashTag(hashTag1);
        meeting.setAdminQnA(adminQnA1);

        String r = s3Uploader.upload(meeting.getId(), multipartFile, "static");
        System.out.println(r);
        return "모임 생성 완료";
    }

    @Transactional
    public List<GetMeetingRes> meetingList(){
        System.out.println("meetingList 들어옴");
        List<Meeting> meetingList = meetingRepository.findAll();
        List<GetMeetingRes> meetingResList = new ArrayList<>();

        for (Meeting mt: meetingList){
            HashTag hashTag = hashTagRepository.findById(mt.getHashTag().getId());
            List<String> tags = new ArrayList<>();
            System.out.println(hashTag.toString()); // hashTag.toString()
            tags.add(hashTag.getTag1());
            tags.add(hashTag.getTag2());
            tags.add(hashTag.getTag3());
            tags.add(hashTag.getTag4());
            tags.add(hashTag.getTag5());
            GetMeetingRes getMeetingRes = GetMeetingRes.builder()
                    .id(mt.getId())
                    .info(mt.getInfo())
                    .image(mt.getImage())
                    .name(mt.getName())
                    .max_num(mt.getMax_num())
                    .cur_num(mt.getCur_num())
                    .onoff(mt.isOnoff())
                    .tags(tags)
                    .build();

            meetingResList.add(getMeetingRes);
        }
        return meetingResList;
    }

    @Transactional(readOnly = true)
    public GetOneMeetingRes oneMeeting(int meeting_id){
        GetOneMeetingRes getOneMeetingRes = new GetOneMeetingRes();
        Meeting meeting = meetingRepository.findById(meeting_id);
        User user = userRepository.findById(meeting.getUserId());

        getOneMeetingRes.setId(meeting.getId());
        getOneMeetingRes.setInfo(meeting.getInfo());
        getOneMeetingRes.setMent(meeting.getMent());
        getOneMeetingRes.setMax_num(meeting.getMax_num());
        getOneMeetingRes.setCur_num(meeting.getCur_num());
        getOneMeetingRes.setImage(meeting.getImage());
        getOneMeetingRes.setEmail(user.getEmail());
        getOneMeetingRes.setName(meeting.getName());
        getOneMeetingRes.setNotice(meeting.getNotice());
        getOneMeetingRes.setOnoff(meeting.isOnoff());

        HashTag hashTag = hashTagRepository.findById(meeting.getHashTag().getId());
        List<String> tags = new ArrayList<>();
        System.out.println(hashTag.toString()); // hashTag.toString()
        tags.add(hashTag.getTag1());
        tags.add(hashTag.getTag2());
        tags.add(hashTag.getTag3());
        tags.add(hashTag.getTag4());
        tags.add(hashTag.getTag5());

        getOneMeetingRes.setTags(tags);
        return getOneMeetingRes;
    }

    @Transactional(readOnly = true)
    public List<GetMeetingRes> myMeetingList(User user){
        System.out.println("myMeetingList 들어옴");
//        List<Meeting> myMeetings = meetingRepository.findByUserId(user.getId());
        List<MeetingUser> meetingUsers = meetingUserRepository.findByUserId(user.getId());

        List<GetMeetingRes> myMeetingList = new ArrayList<>();

        for (MeetingUser mt: meetingUsers){
            if (mt.getStatus().equals("승인") || mt.getStatus().equals("모임장")) {
                List<String> tag = new ArrayList<>();
                tag.add(mt.getMeeting().getHashTag().getTag1());
                tag.add(mt.getMeeting().getHashTag().getTag2());
                tag.add(mt.getMeeting().getHashTag().getTag3());
                tag.add(mt.getMeeting().getHashTag().getTag4());
                tag.add(mt.getMeeting().getHashTag().getTag5());
                User user1 = userRepository.findById(mt.getMeeting().getUserId());

                GetMeetingRes getMeetingRes = GetMeetingRes.builder()
                        .id(mt.getMeeting().getId())
                        .info(mt.getMeeting().getInfo())
                        .image(mt.getMeeting().getImage())
                        .name(mt.getMeeting().getName())
                        .max_num(mt.getMeeting().getMax_num())
                        .cur_num(mt.getMeeting().getCur_num())
                        .onoff(mt.getMeeting().isOnoff())
                        .username(user.getUsername())
                        .email(user1.getEmail())
                        .tags(tag)
                        .build();
                myMeetingList.add(getMeetingRes);
            }
        }

        return myMeetingList;
    }

    @Transactional
    public String addMeeting(User user, Meeting meeting, PostAnswerReq postAnswerReq){
        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setUser(user);
        meetingUser.setMeeting(meeting);
        meetingUser.setStatus("수락 대기");
        // meeting.setCur_num(meeting.getCur_num()+1);  // 인원 추가
        meetingUserRepository.save(meetingUser);

        UserQnA userQnA = new UserQnA();
//
//        userQnA.setA1(postAnswerReq.getA1());
//        userQnA.setA2(postAnswerReq.getA2());
//        userQnA.setA3(postAnswerReq.getA3());
//        userQnA.setA4(postAnswerReq.getA4());
//        userQnA.setA5(postAnswerReq.getA5());
        userQnA.setA1(postAnswerReq.getAnswers().get(0));
        userQnA.setA2(postAnswerReq.getAnswers().get(1));
        userQnA.setA3(postAnswerReq.getAnswers().get(2));
        userQnA.setA4(postAnswerReq.getAnswers().get(3));
        userQnA.setA5(postAnswerReq.getAnswers().get(4));

        userQnA.setUserId(user.getId());

        // 모임 id
        AdminQnA adminQnA = adminQnARepository.findByMeetingId(meeting.getId());
        userQnA.setAdminQnA(adminQnA);
        userQnARepository.save(userQnA);

        return "모임 가입 신청 완료";
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
    public MeetingRes questionList(int meeting_id){
        // 미팅을 가져와서
        AdminQnA question = adminQnARepository.findByMeetingId(meeting_id);
        Meeting meeting = meetingRepository.findById(meeting_id);
        List<String> questionList = new ArrayList<>();

        questionList.add(question.getQ1());
        questionList.add(question.getQ2());
        questionList.add(question.getQ3());
        questionList.add(question.getQ4());
        questionList.add(question.getQ5());
//        System.out.println("질문 리스트 출력 : " + questions);

        MeetingRes meetingRes = MeetingRes.builder()
                .name(meeting.getName())
                .questions(questionList).build();
        return meetingRes;
    }

//    @Transactional
//    public String createAnswer(User user, int id, PostAnswerReq postAnswerReq){
//        UserQnA userQnA = new UserQnA();
//
//        userQnA.setA1(postAnswerReq.getA1());
//        userQnA.setA2(postAnswerReq.getA2());
//        userQnA.setA3(postAnswerReq.getA3());
//        userQnA.setA4(postAnswerReq.getA4());
//        userQnA.setA5(postAnswerReq.getA5());
//        userQnA.setUserId(user.getId());
//
//        // 모임 id
//        AdminQnA adminQnA = adminQnARepository.findByMeetingId(id);
//        userQnA.setAdminQnA(adminQnA);
//        userQnARepository.save(userQnA);
//        return "답변 생성 완료";
//    }


//    @Transactional
//    public String createAnswer(User user, int meeting_id, PostAnswerListReq postAnswerListReq){
//        System.out.println("서비스 들어옴");
//        UserQnA userQnA = new UserQnA();
//
//        List<PostAnswerReq> postAnswerReqList = new ArrayList<>();
//        PostAnswerReq post1 = postAnswerListReq.getPostAnswerReqs().get(0);
//        System.out.println("get");
//        postAnswerReqList.add(post1);
//
//        PostAnswerReq postAnswerReq = new PostAnswerReq();
//
//        postAnswerReq.setA1(postAnswerListReq.getPostAnswerReqs().get(0).getA1());
//        postAnswerReq.setA2(postAnswerListReq.getPostAnswerReqs().get(0).getA2());
//        postAnswerReq.setA3(postAnswerListReq.getPostAnswerReqs().get(0).getA3());
//        postAnswerReq.setA4(postAnswerListReq.getPostAnswerReqs().get(0).getA4());
//        postAnswerReq.setA5(postAnswerListReq.getPostAnswerReqs().get(0).getA5());
//
//        System.out.println("==========================");
//        userQnA.setA1(postAnswerReq.getA1());
//        userQnA.setA2(postAnswerReq.getA2());
//        userQnA.setA3(postAnswerReq.getA3());
//        userQnA.setA4(postAnswerReq.getA4());
//        userQnA.setA5(postAnswerReq.getA5());
//        userQnA.setUserId(user.getId());
//        userQnARepository.save(userQnA);
//
//        // 모임 id
//        AdminQnA adminQnA = adminQnARepository.findByMeetingId(meeting_id);
//        userQnA.setAdminQnA(adminQnA);
//        userQnARepository.save(userQnA);
//
//        System.out.println("=================저장");
//        System.out.println("postAnswerListReq : " + postAnswerListReq);
//
//        return "모임 답변 생성 완료";
//
//    }

    @Transactional
    public String createNotice(int meeting_id, User user, PostNoticeReq postNoticeReq){

        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()) {
            System.out.println(meeting.getName());
            System.out.println(postNoticeReq.getNotice());

            meeting.setNotice(postNoticeReq.getNotice());
            meetingRepository.save(meeting);
            return "공지 생성 완료";
        }
        return null;
    }

    @Transactional
    public String deleteNotice(int meeting_id, User user){
        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()){
            meeting.setNotice(null);
            return "공지 삭제 완료";
        }
        return null;
    }


    @Transactional(readOnly = true)
    public List<GetAnswerRes> answerList(int meeting_id){  // 모임 답변 전체 조회 api
        System.out.println("서비스 들어옴");
        Meeting meeting = meetingRepository.findById(meeting_id);

        List<UserQnA> userQnAList =  userQnARepository.findByAdminQnAId(meeting.getAdminQnA().getId());
//        List<AdminQnA> adminQnAList = adminQnARepository.findByMeetingId2(meeting_id);
        AdminQnA adminQnA = adminQnARepository.findById(meeting.getAdminQnA().getId());

        List<GetAnswerRes> getAnswerResList =  new ArrayList<>();

        for (UserQnA userqna : userQnAList){
            GetAnswerRes getAnswerRes = new GetAnswerRes();


            User user = userRepository.findById(userqna.getUserId());
            getAnswerRes.setUsername(user.getUsername());
            getAnswerRes.setUser_id(user.getId());
            getAnswerRes.setEmail(user.getEmail());
            // 질문 배열
            List<String> qnaList = new ArrayList<>();
            qnaList.add(adminQnA.getQ1());
            qnaList.add(adminQnA.getQ2());
            qnaList.add(adminQnA.getQ3());
            qnaList.add(adminQnA.getQ4());
            qnaList.add(adminQnA.getQ5());

            getAnswerRes.setQuestions(qnaList);
            // 답변 배열
            List<String> answerList = new ArrayList<>();
            answerList.add(userqna.getA1());
            answerList.add(userqna.getA2());
            answerList.add(userqna.getA3());
            answerList.add(userqna.getA4());
            answerList.add(userqna.getA5());

            getAnswerRes.setAnswers(answerList);

            getAnswerResList.add(getAnswerRes);
        }
        return getAnswerResList;
    }


    @Transactional(readOnly = true)
    public GetAnswerRes oneAnswer(User user, int meeting_id){
        // user랑 meeting_id 넘겨받아서
        AdminQnA adminQnA = adminQnARepository.findByMeetingId(meeting_id);
        UserQnA userQnA = userQnARepository.findByUserIdAndAdminQnAId(user.getId(), adminQnA.getId());

        GetAnswerRes getMeetingRes = new GetAnswerRes();

        getMeetingRes.setUsername(user.getUsername());
        getMeetingRes.setEmail(user.getEmail());

        List<String> answers = new ArrayList<>();
        List<String> questions = new ArrayList<>();

        answers.add(userQnA.getA1());
        answers.add(userQnA.getA1());
        answers.add(userQnA.getA1());
        answers.add(userQnA.getA1());
        answers.add(userQnA.getA1());

        questions.add(adminQnA.getQ1());
        questions.add(adminQnA.getQ2());
        questions.add(adminQnA.getQ3());
        questions.add(adminQnA.getQ4());
        questions.add(adminQnA.getQ5());

        getMeetingRes.setAnswers(answers);
        getMeetingRes.setQuestions(questions);

        return getMeetingRes;
    }

    @Transactional
    public String acceptAnswer(User user, int meeting_id, int answer_id){
        Meeting meeting = meetingRepository.findById(meeting_id);
        UserQnA userQnA = userQnARepository.findById(answer_id);
        if (meeting.getUserId() == user.getId()){
            MeetingUser meetingUser = meetingUserRepository.findByUserIdAndMeetingId(userQnA.getUserId(), meeting.getId());
            meetingUser.setStatus("승인");
            meeting.setCur_num(meeting.getCur_num() + 1);
            return "모임 답변 수락 완료";
        }
        return "모임장만 수락할 수 있습니다.";
    }

    @Transactional
    public String deleteAnswer(User user, int meeting_id, int answer_id){
        UserQnA userQnA = userQnARepository.findById(answer_id);
        System.out.println(userQnA);
        System.out.println(userQnA.getUserId());
        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()){ // 삭제하려는 사람이 모임 만든사람인지 확인
            //UserQnA userQnA1 = userQnARepository.findByUserIdAndAdminQnAId(userQnA.getId(), userQnA.getAdminQnA().getId());
            userQnARepository.delete(userQnA);
            MeetingUser meetingUser = meetingUserRepository.findByUserIdAndMeetingId(userQnA.getUserId(), meeting.getId());
            meetingUser.setStatus("거절");
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

    @Transactional(readOnly = true)
    public String check(int meeting_id, User user){
        System.out.println("$$$$$$$$$$$$$$$$$$$$" + meeting_id);
        MeetingUser meetingUser = meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting_id);
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&" + meetingUser);
        if (meetingUser.getStatus().equals("승인") || meetingUser.getStatus().equals("모임장")) {
            return "가입";
        }else{
            return "미가입";
        }
    }
}