package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.QnA.AdminQnARepository;
import com.dormammu.BooklogWeb.domain.QnA.UserQnA;
import com.dormammu.BooklogWeb.domain.QnA.UserQnARepository;
import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.comment.CommentRepository;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import com.dormammu.BooklogWeb.domain.hastag.HashTagRepository;
import com.dormammu.BooklogWeb.domain.meeting.*;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final AdminQnARepository adminQnARepository;
    private final HashTagRepository hashTagRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final UserQnARepository userQnARepository;
    private final MeetingDateRepository meetingDateRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public String createMeeting(User user, MultipartFile multipartFile, String name, String info, String ment, String max_num, String onoff, List<String> questions, List<String> hashtags) throws IOException {
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
        meetingUser.setStatus("?????????");
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);
        adminQnARepository.save(adminQnA);
        hashTagRepository.save(hashTag);
        meetingRepository.save(meeting);
        AdminQnA adminQnA1 = adminQnARepository.findById(adminQnA.getId());
        HashTag hashTag1 = hashTagRepository.findById(hashTag.getId());

        meeting.setHashTag(hashTag1);
        meeting.setAdminQnA(adminQnA1);

        String r = s3Uploader.upload(meeting.getId(), multipartFile, "meeting");
        System.out.println(r);
        return "?????? ?????? ??????";
    }

    @Transactional
    public List<GetMeetingRes> meetingList(){
        System.out.println("meetingList ?????????");
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

        getOneMeetingRes.setId(meeting.getId());
        getOneMeetingRes.setInfo(meeting.getInfo());
        getOneMeetingRes.setMent(meeting.getMent());
        getOneMeetingRes.setMax_num(meeting.getMax_num());
        getOneMeetingRes.setCur_num(meeting.getCur_num());
        getOneMeetingRes.setImage(meeting.getImage());
        getOneMeetingRes.setName(meeting.getName());
        getOneMeetingRes.setNotice(meeting.getNotice());
        getOneMeetingRes.setOnoff(meeting.isOnoff());

        // ????????????
        HashTag hashTag = hashTagRepository.findById(meeting.getHashTag().getId());
        List<String> tags = new ArrayList<>();
        System.out.println(hashTag.toString()); // hashTag.toString()
        tags.add(hashTag.getTag1());
        tags.add(hashTag.getTag2());
        tags.add(hashTag.getTag3());
        tags.add(hashTag.getTag4());
        tags.add(hashTag.getTag5());

        getOneMeetingRes.setTags(tags);

        // ??????
        List<MeetingDate> meetingDate = meetingDateRepository.findByMeetingId(meeting_id);

        List<String> dates = new ArrayList<>();
        for (MeetingDate md : meetingDate) {
            dates.add(md.getDate());
        }
        getOneMeetingRes.setDates(dates);

        return getOneMeetingRes;
    }

    @Transactional(readOnly = true)
    public List<GetMeetingRes> myMeetingList(User user){
        System.out.println("myMeetingList ?????????");
//        List<Meeting> myMeetings = meetingRepository.findByUserId(user.getId());
        List<MeetingUser> meetingUsers = meetingUserRepository.findByUserId(user.getId());

        List<GetMeetingRes> myMeetingList = new ArrayList<>();

        for (MeetingUser mt: meetingUsers){
            if (mt.getStatus().equals("??????") || mt.getStatus().equals("?????????")) {
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

        Optional<MeetingUser> meetingUser =  meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting.getId());

        if (!meetingUser.isPresent()) {
            MeetingUser newMeetingUser = new MeetingUser();
            newMeetingUser.setUser(user);
            newMeetingUser.setMeeting(meeting);
            newMeetingUser.setStatus("?????? ??????");
            // meeting.setCur_num(meeting.getCur_num()+1);  // ?????? ??????
            meetingUserRepository.save(newMeetingUser);

            UserQnA userQnA = new UserQnA();
            userQnA.setA1(postAnswerReq.getAnswers().get(0));
            userQnA.setA2(postAnswerReq.getAnswers().get(1));
            userQnA.setA3(postAnswerReq.getAnswers().get(2));
            userQnA.setA4(postAnswerReq.getAnswers().get(3));
            userQnA.setA5(postAnswerReq.getAnswers().get(4));

            userQnA.setUserId(user.getId());

            // ?????? id
            AdminQnA adminQnA = adminQnARepository.findByMeetingId(meeting.getId());
            userQnA.setAdminQnA(adminQnA);
            userQnARepository.save(userQnA);

            return "?????? ?????? ?????? ??????";
        } else {
            return "?????? ?????? ????????? ???????????????.";
        }

    }

    @Transactional
    public String outMeeting(User user, Meeting meeting){

        Optional<MeetingUser> meetingUser =  meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting.getId());
        System.out.println(user.getId() +" AND " + meeting.getId());
        meeting.setCur_num(meeting.getCur_num()-1);
        meetingUserRepository.delete(meetingUser.get());

        return "?????? ?????? ??????";
    }

    @Transactional
    public String update(int id, PatchMeetingReq patchMeetingReq){
        Meeting meeting = meetingRepository.findById(id);
//        System.out.println(meeting.getName() + "??? ?????? ????????? ???????????????");
        meeting.setName(patchMeetingReq.getName());
        meeting.setInfo(patchMeetingReq.getInfo());
        meeting.setMent(patchMeetingReq.getMent());
        meeting.setImage(patchMeetingReq.getImage());
        meeting.setOnoff(patchMeetingReq.isOnoff());
        meeting.setMax_num(patchMeetingReq.getMax_num());

        return "?????? ?????? ?????? ??????";
    }

    @Transactional
    public String deleteMeeting(User user, Meeting meeting){
        System.out.println("User :" + user.getUsername() + ", " + "Meeting : " + meeting.getName());
        meetingRepository.delete(meeting);
        System.out.println("?????? ?????????");

//        System.out.println("????????????, ????????????, ???????????? ?????????");  // ?????? ?????? -> meetingUser??????, ????????????, ????????????
        return "?????? ?????? ??????";
    }

    @Transactional(readOnly = true)
    public MeetingRes questionList(int meeting_id){
        // ????????? ????????????
        AdminQnA question = adminQnARepository.findByMeetingId(meeting_id);
        Meeting meeting = meetingRepository.findById(meeting_id);
        List<String> questionList = new ArrayList<>();

        questionList.add(question.getQ1());
        questionList.add(question.getQ2());
        questionList.add(question.getQ3());
        questionList.add(question.getQ4());
        questionList.add(question.getQ5());
//        System.out.println("?????? ????????? ?????? : " + questions);

        MeetingRes meetingRes = MeetingRes.builder()
                .name(meeting.getName())
                .questions(questionList).build();
        return meetingRes;
    }

    @Transactional
    public String createNotice(int meeting_id, User user, PostNoticeReq postNoticeReq){

        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()) {
            System.out.println(meeting.getName());
            System.out.println(postNoticeReq.getNotice());

            meeting.setNotice(postNoticeReq.getNotice());
            meetingRepository.save(meeting);
            return "?????? ?????? ??????";
        }
        return null;
    }

    @Transactional
    public String deleteNotice(int meeting_id, User user){
        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()){
            meeting.setNotice(null);
            return "?????? ?????? ??????";
        }
        return null;
    }


    @Transactional(readOnly = true)
    public List<GetAnswerRes> answerList(int meeting_id, User loginUser) {  // ?????? ?????? ?????? ?????? api

        Meeting meeting = meetingRepository.findById(meeting_id);

        // ????????? ????????? ?????? ??????????????? ??????
        if (loginUser.getId() == meeting.getUserId()) {

            List<MeetingUser> meetingUsers = meetingUserRepository.findByMeetingId(meeting_id);

            List<GetAnswerRes> getAnswerResList = new ArrayList<>();

            for (MeetingUser meetingUser : meetingUsers) {
                if (meetingUser.getStatus().equals("?????? ??????")) {

                    List<UserQnA> userQnAList = userQnARepository.findByAdminQnAIdAndUserId(meeting.getAdminQnA().getId(), meetingUser.getUser().getId());
                    AdminQnA adminQnA = adminQnARepository.findByMeetingId(meetingUser.getMeeting().getId());

                    for (UserQnA userqna : userQnAList) {
                        GetAnswerRes getAnswerRes = new GetAnswerRes();

                        User user = userRepository.findById(userqna.getUserId());

                        getAnswerRes.setQna_id(userqna.getId());
                        getAnswerRes.setUsername(user.getUsername());
                        getAnswerRes.setUser_id(user.getId());
                        getAnswerRes.setEmail(user.getEmail());

                        // ?????? ??????
                        List<String> qnaList = new ArrayList<>();
                        qnaList.add(adminQnA.getQ1());
                        qnaList.add(adminQnA.getQ2());
                        qnaList.add(adminQnA.getQ3());
                        qnaList.add(adminQnA.getQ4());
                        qnaList.add(adminQnA.getQ5());

                        getAnswerRes.setQuestions(qnaList);

                        // ?????? ??????
                        List<String> answerList = new ArrayList<>();
                        answerList.add(userqna.getA1());
                        answerList.add(userqna.getA2());
                        answerList.add(userqna.getA3());
                        answerList.add(userqna.getA4());
                        answerList.add(userqna.getA5());

                        getAnswerRes.setAnswers(answerList);

                        getAnswerResList.add(getAnswerRes);
                    }

                }
            }
            return getAnswerResList;
        }
        else {
            return null;
        }
    }


    @Transactional(readOnly = true)
    public GetAnswerRes oneAnswer(User user, int meeting_id){
        // user??? meeting_id ???????????????
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
            Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(userQnA.getUserId(), meeting.getId());
            meetingUser.get().setStatus("??????");
            meeting.setCur_num(meeting.getCur_num() + 1);
            return "?????? ?????? ?????? ??????";
        }
        return "???????????? ????????? ??? ????????????.";
    }

    @Transactional
    public String deleteAnswer(User user, int meeting_id, int answer_id){
        UserQnA userQnA = userQnARepository.findById(answer_id);
        System.out.println(userQnA);
        System.out.println(userQnA.getUserId());
        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()){ // ??????????????? ????????? ?????? ?????????????????? ??????
            //UserQnA userQnA1 = userQnARepository.findByUserIdAndAdminQnAId(userQnA.getId(), userQnA.getAdminQnA().getId());
            userQnARepository.delete(userQnA);
            Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(userQnA.getUserId(), meeting.getId());
            meetingUser.get().setStatus("??????");
            return "?????? ?????? ?????? ??????";
        }
        return null;
    }

    @Transactional
    public String outUser(User user, int meeting_id, int user_id){

        Meeting meeting = meetingRepository.findById(meeting_id);
        // ?????????????????? ?????? ????????? , meeting-user?????? ?????????
        if (meeting.getUserId() == user.getId()) {  // ?????? ??????????????? ??????????????? ???????????? ???????????????
            meeting.setCur_num(meeting.getCur_num() - 1); // ????????? -1

            Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(user_id, meeting_id);
            meetingUserRepository.delete(meetingUser.get());
            return "?????? ?????? ??????";
        }
        return null;
    }

    @Transactional(readOnly = true)
    public int check(int meeting_id, User user){
        Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting_id);

        if (!meetingUser.isPresent() || meetingUser.get().getStatus().equals("??????")){
            return 0;
        } else {
            if (meetingUser.get().getStatus().equals("??????")) {
                return 2;
            } else if (meetingUser.get().getStatus().equals("?????????")) {
                return 3;
            }
            else {
                return 1;
            }
        }
    }

    @Transactional(readOnly = false)
    public String attendance(int meeting_id, String date, User user){
        Meeting meeting = meetingRepository.findById(meeting_id);

        if (meeting.getUserId() == user.getId()) {
            MeetingDate meetingDate = new MeetingDate();
            meetingDate.setMeeting(meeting);
            meetingDate.setDate(date);

            meetingDateRepository.save(meetingDate);

            return "?????? ??????";
        }
        else {
            return "???????????? ?????? ????????? ????????? ??? ????????????.";
        }
    }

    @Transactional(readOnly = true)
    public GetNoticeRes notice(int meeting_id){

        Meeting meeting = meetingRepository.findById(meeting_id);

        List<Comment> commentList = commentRepository.findByMeetingId(meeting_id);

        List<GetCommentRes> getCommentResList = new ArrayList<>();

        for (Comment cm: commentList){
            GetCommentRes getCommentRes = GetCommentRes.builder()
                    .content(cm.getContent())
                    .comment_id(cm.getId())
                    .username(cm.getUser().getUsername())
                    .email(cm.getUser().getEmail())
                    .build();
            getCommentResList.add(getCommentRes);
        }

        GetNoticeRes getNoticeRes = GetNoticeRes.builder()
                .notice(meeting.getNotice())
                .getCommentResList(getCommentResList).build();
        return getNoticeRes;
    }
}