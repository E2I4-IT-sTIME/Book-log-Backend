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
    private final EntityManagerQuery entityManagerQuery;

    @Transactional(readOnly = true)
    public GetMeetingDesRes meetingMain(User user, int meeting_id){
        List<MeetingUser> byMeetingId = meetingUserRepository.findByMeetingId(meeting_id);
        Meeting meeting = meetingRepository.findById(meeting_id);
        List<Comment> commentList = commentRepository.findByMeetingId(meeting_id);
        List<GetCommentRes> getCommentResList = new ArrayList<>();

        GetMeetingDesRes getMeetingDesRes = new GetMeetingDesRes();
        for (Comment cm: commentList){
            GetCommentRes getCommentRes = GetCommentRes.builder()
                    .createDate(cm.getCreatedDate())
                    .content(cm.getContent())
                    .comment_id(cm.getId())
                    .username(cm.getUser().getUsername())
                    .email(cm.getUser().getEmail())
                    .build();
            getCommentResList.add(getCommentRes);
        }

        // 공지랑 댓글 리스트
        GetNoticeRes getNoticeRes = GetNoticeRes.builder()
                .createDate(meeting.getCreateDate())
                .notice(meeting.getNotice())
                .getCommentResList(getCommentResList).build();

        List<String> userImage = new ArrayList<>();
        for (MeetingUser mu: byMeetingId){
            if (mu.getStatus().equals("승인") || mu.getStatus().equals("모임장")){
                userImage.add(mu.getUser().getImgPath());
            }
        }

        List<String> tags = new ArrayList<>();
        tags.add(meeting.getHashTag().getTag1());
        tags.add(meeting.getHashTag().getTag2());
        tags.add(meeting.getHashTag().getTag3());
        tags.add(meeting.getHashTag().getTag4());
        tags.add(meeting.getHashTag().getTag5());

        getMeetingDesRes.setUserImage(userImage);
        getMeetingDesRes.setGetNoticeRes(getNoticeRes);
        getMeetingDesRes.setName(meeting.getName());
        getMeetingDesRes.setInfo(meeting.getInfo());
        getMeetingDesRes.setOnoff(meeting.isOnoff());
        getMeetingDesRes.setCur_num(meeting.getCur_num());
        getMeetingDesRes.setTagList(tags);
        return getMeetingDesRes;
    }

    @Transactional
    public String createMeeting(User user, MultipartFile multipartFile, String name, String info, String ment, String max_num, String onoff, ArrayList<String> questions, ArrayList<String> hashtags) throws IOException {
        Meeting meeting = new Meeting();
        meeting.setName(name);
        meeting.setInfo(info);
        meeting.setMent(ment);
        meeting.setUserId(user.getId());
        meeting.setCur_num(1);
        meeting.setMax_num(Integer.parseInt(max_num));
        meeting.setOnoff(Boolean.parseBoolean(onoff));

        AdminQnA adminQnA = new AdminQnA();
        adminQnA.setMeeting(meeting);

        while (questions.size() != 5){
            questions.add(null);
        }

        adminQnA.setQ1(questions.get(0));
        adminQnA.setQ2(questions.get(1));
        adminQnA.setQ3(questions.get(2));
        adminQnA.setQ4(questions.get(3));
        adminQnA.setQ5(questions.get(4));

        HashTag hashTag = new HashTag();
        hashTag.setMeeting(meeting);

        while (hashtags.size() != 5){
            hashtags.add(null);
        }
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

        String r = s3Uploader.upload(meeting.getId(), multipartFile, "meeting");
        System.out.println(r);
        return "모임 생성 완료";
    }

    @Transactional
    public List<GetMeetingRes> meetingList(){
        List<Meeting> meetingList = meetingRepository.findAll();
        List<GetMeetingRes> meetingResList = new ArrayList<>();

        for (Meeting mt: meetingList){
            HashTag hashTag = hashTagRepository.findById(mt.getHashTag().getId());
            List<String> tags = new ArrayList<>();
            User userEntity = userRepository.findById(mt.getUserId());
            System.out.println(hashTag.toString()); // hashTag.toString()

            tags.add(hashTag.getTag1());
            tags.add(hashTag.getTag2());
            tags.add(hashTag.getTag3());
            tags.add(hashTag.getTag4());
            tags.add(hashTag.getTag5());
            GetMeetingRes getMeetingRes = GetMeetingRes.builder()
                    .id(mt.getId())
                    .image(mt.getImage())
                    .name(mt.getName())
                    .max_num(mt.getMax_num())
                    .cur_num(mt.getCur_num())
                    .onoff(mt.isOnoff())
                    .username(userEntity.getUsername())
                    .tags(tags)
                    .info(mt.getInfo())
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

        // 해시태그
        HashTag hashTag = hashTagRepository.findById(meeting.getHashTag().getId());
        List<String> tags = new ArrayList<>();
        System.out.println(hashTag.toString()); // hashTag.toString()
        tags.add(hashTag.getTag1());
        tags.add(hashTag.getTag2());
        tags.add(hashTag.getTag3());
        tags.add(hashTag.getTag4());
        tags.add(hashTag.getTag5());

        getOneMeetingRes.setTags(tags);

        // 날짜
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
        System.out.println("myMeetingList 들어옴");
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
                        .image(mt.getMeeting().getImage())
                        .name(mt.getMeeting().getName())
                        .max_num(mt.getMeeting().getMax_num())
                        .cur_num(mt.getMeeting().getCur_num())
                        .onoff(mt.getMeeting().isOnoff())
                        .username(user.getUsername())
                        .info(mt.getMeeting().getInfo())
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
            newMeetingUser.setStatus("수락 대기");
            meetingUserRepository.save(newMeetingUser);

            UserQnA userQnA = new UserQnA();

            while (postAnswerReq.getAnswers().size() != 5){
                postAnswerReq.getAnswers().add(null);
            }
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
        } else {
            return "이미 가입 신청한 유저입니다.";
        }

    }

    @Transactional
    public String outMeeting(User user, Meeting meeting){

        Optional<MeetingUser> meetingUser =  meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting.getId());
        System.out.println(user.getId() +" AND " + meeting.getId());
        meeting.setCur_num(meeting.getCur_num()-1);
        meetingUserRepository.delete(meetingUser.get());

        return "모임 탈퇴 완료";
    }

    @Transactional
    public String update(int id, PatchMeetingReq patchMeetingReq){
        Meeting meeting = meetingRepository.findById(id);
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

        MeetingRes meetingRes = MeetingRes.builder()
                .name(meeting.getName())
                .questions(questionList).build();
        return meetingRes;
    }

    @Transactional
    public String createNotice(int meeting_id, User user, PostNoticeReq postNoticeReq){

        Meeting meeting = meetingRepository.findById(meeting_id);
        if (meeting.getUserId() == user.getId()) {
//            System.out.println(meeting.getName());
//            System.out.println(postNoticeReq.getNotice());

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
    public List<GetAnswerRes> answerList(int meeting_id, User loginUser) {  // 모임 답변 전체 조회 api

        Meeting meeting = meetingRepository.findById(meeting_id);

        // 접근한 사람이 모임 관리자인지 확인
        if (loginUser.getId() == meeting.getUserId()) {

            List<MeetingUser> meetingUsers = meetingUserRepository.findByMeetingId(meeting_id);

            List<GetAnswerRes> getAnswerResList = new ArrayList<>();

            for (MeetingUser meetingUser : meetingUsers) {
                if (meetingUser.getStatus().equals("수락 대기")) {

                    List<UserQnA> userQnAList = userQnARepository.findByAdminQnAIdAndUserId(meeting.getAdminQnA().getId(), meetingUser.getUser().getId());
                    AdminQnA adminQnA = adminQnARepository.findByMeetingId(meetingUser.getMeeting().getId());

                    for (UserQnA userqna : userQnAList) {
                        GetAnswerRes getAnswerRes = new GetAnswerRes();

                        User user = userRepository.findById(userqna.getUserId());

                        getAnswerRes.setQna_id(userqna.getId());
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
            Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(userQnA.getUserId(), meeting.getId());
            meetingUser.get().setStatus("승인");
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
            Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(userQnA.getUserId(), meeting.getId());
            meetingUser.get().setStatus("거절");
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

            Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(user_id, meeting_id);
            meetingUserRepository.delete(meetingUser.get());
            return "모임 강퇴 완료";
        }
        return null;
    }

    @Transactional(readOnly = true)
    public int check(int meeting_id, User user){
        Optional<MeetingUser> meetingUser = meetingUserRepository.findByUserIdAndMeetingId(user.getId(), meeting_id);

        if (!meetingUser.isPresent() || meetingUser.get().getStatus().equals("거절")){
            return 0;
        } else {
            if (meetingUser.get().getStatus().equals("승인")) {
                return 2;
            } else if (meetingUser.get().getStatus().equals("모임장")) {
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

            return "출석 완료";
        }
        else {
            return "모임장만 출석 완료를 체크할 수 있습니다.";
        }
    }

    @Transactional(readOnly = true)
    public GetNoticeRes notice(int meeting_id){

        Meeting meeting = meetingRepository.findById(meeting_id);

        List<Comment> commentList = commentRepository.findByMeetingId(meeting_id);

        List<GetCommentRes> getCommentResList = new ArrayList<>();

        for (Comment cm: commentList){
            GetCommentRes getCommentRes = GetCommentRes.builder()
                    .createDate(cm.getCreatedDate())
                    .content(cm.getContent())
                    .comment_id(cm.getId())
                    .username(cm.getUser().getUsername())
                    .email(cm.getUser().getEmail())
                    .build();
            getCommentResList.add(getCommentRes);
        }

        GetNoticeRes getNoticeRes = GetNoticeRes.builder()
                .createDate(meeting.getCreateDate())
                .notice(meeting.getNotice())
                .getCommentResList(getCommentResList).build();
        return getNoticeRes;
    }

    @Transactional(readOnly = true)
    public List<GetmNameRes> searchMeeting(String name){
        List<GetmNameRes> getmNameRes = entityManagerQuery.mfindBymName(name);
//        for (GetmNameRes gm: getmNameRes){
//            Meeting meeting = meetingRepository.findById(gm.getId());
//            HashTag tag = hashTagRepository.findById(meeting.getHashTag().getId());
//            List<String> tags = new ArrayList<>();
//            tags.add(tag.getTag1());
//            tags.add(tag.getTag2());
//            tags.add(tag.getTag3());
//            tags.add(tag.getTag4());
//            tags.add(tag.getTag5());
//
//            //gm.setTags(tags);
//        }

        return getmNameRes;
    }

    @Transactional(readOnly = true)
    public List<GetCategoryRes> searchCategory(String tagName){
        List<GetCategoryRes> getCategoryRes = entityManagerQuery.mfindByCategory(tagName);
        return getCategoryRes;
    }
}