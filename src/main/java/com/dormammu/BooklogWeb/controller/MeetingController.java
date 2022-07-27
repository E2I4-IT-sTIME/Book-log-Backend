package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import com.dormammu.BooklogWeb.service.MeetingService;
import com.dormammu.BooklogWeb.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final S3Uploader s3Uploader;

//    @PostMapping("/auth/meeting")  // 모임 생성 API(+이미지)
//    public String createMeeting(Authentication authentication,
//                                @RequestPart(value = "image") MultipartFile multipartFile, @RequestPart(value = "postMeetingReq")PostMeetingReq postMeetingReq) throws IOException {
//
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        User user = userRepository.findById(principalDetails.getUser().getId());
////        meetingService.createMeeting(principalDetails.getUser(), postMeetingReq);
////        FileUploadResponse meetingImage = s3Uploader.upload(meeting_id, multipartFile, "static");
////        return ResponseEntity.ok().body(meetingImage);
//        return meetingService.createMeeting(user, multipartFile, postMeetingReq);
//    }

    /* 테스트 */
    @PostMapping("/auth/meeting")  // 모임 생성 API(+이미지)
    public String createMeeting(Authentication authentication,
                                @RequestPart(value = "image") MultipartFile multipartFile, @RequestParam("name") String name, @RequestParam("info") String info,
                                @RequestParam("ment") String ment, @RequestParam("max_num") String max_num, @RequestParam("onoff") String onoff,
                                @RequestParam("questions")List<String> questions, @RequestParam("hashtags")List<String> hashtags) throws IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());
        return meetingService.createMeeting(user, multipartFile, name, info, ment, max_num, onoff, questions, hashtags);
    }




//    @PostMapping("/images")  // 이미지 업로드하기
//    public ResponseEntity<?> images(@RequestPart("images") MultipartFile multipartFile) throws IOException {
//        FileUploadResponse image = s3Uploader.upload(multipartFile, "static");
//        return ResponseEntity.ok(image);
//    }

    @GetMapping("/meetings")  // 모임 리스트 조회 API
    public List<GetMeetingRes> meetingList(){
        return meetingService.meetingList();
    }

    @GetMapping("/meetings/{meeting_id}")  // 모임 개별 조회 API
    public GetOneMeetingRes oneMeeting(@PathVariable int meeting_id){
        return meetingService.oneMeeting(meeting_id);
    }

    @GetMapping("/auth/user/{id}/meetings")  // 내 모임 조회 API
    public List<GetMeetingRes> myMeetingList(@PathVariable int id, Authentication authentication){

        User user = userRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (user.getId() == principalDetails.getUser().getId()){
            System.out.println("현재 로그인된 유저 : " + principalDetails.getUser().getUsername());
            return meetingService.myMeetingList(user);
        }
        return null;
    }

    @PostMapping("/auth/meetings/{id}")  // 모임 가입 신청 API
    public String addMeeting(@RequestBody PostAnswerReq postAnswerReq, @PathVariable int id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        return meetingService.addMeeting(user, meeting, postAnswerReq);

    }

    @DeleteMapping("/auth/meeting/{id}/out")  // 모임 탈퇴 API
    public String outMeeting(@PathVariable int id, Authentication authentication){
        Meeting meeting = meetingRepository.findById(id);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.outMeeting(user, meeting);
        }
        return null;
    }

    @PatchMapping("/auth/meeting/{id}")  // 모임 수정 API
    public String updateMeeting(@PathVariable int id, Authentication authentication, @RequestBody PatchMeetingReq patchMeetingReq){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(meeting.getUserId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.update(id, patchMeetingReq);  // 모임 id 넘겨줌
        }
        return null;
    }

    @DeleteMapping("/auth/meeting/{id}")  // 모임 삭제 API
    public String deleteMeeting(@PathVariable int id, Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        if(user.getId() == principalDetails.getUser().getId()){
            return meetingService.deleteMeeting(user, meeting);
        }
        return null;
    }

    @GetMapping("/auth/{meeting_id}/question")  // 모임 질문 조회 API
    public MeetingRes questionList(@PathVariable int meeting_id, Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        return meetingService.questionList(meeting_id);
    }

    @GetMapping("/auth/meetings/{meeting_id}/answers")  // 모임 답변 전체 조회 api
    public List<GetAnswerRes> answerList(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(meeting_id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        // 접근한 사람이 모임 관리자인지 확인
        if (user.getId() == meeting.getUserId()) {
            return meetingService.answerList(meeting_id);
        }
        return null;
    }


    @GetMapping("/auth/meeting/{meeting_id}/answers/{user_id}")  // 모임 답변 개별 조회 api -> 사용 x 예정
    public GetAnswerRes oneAnswer(@PathVariable int meeting_id, Authentication authentication, @PathVariable int user_id){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(user_id);


        return meetingService.oneAnswer(user,meeting_id);

    }

    @PostMapping("/auth/{meeting_id}/answer/{answer_id}")  // 모임 답변 수락 api (가입 수락)
    public String acceptAnswer(@PathVariable int meeting_id, @PathVariable int answer_id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.acceptAnswer(user, meeting_id, answer_id);
        }
        return "모임장만 수락할 수 있습니다.";
    }

    @DeleteMapping("/auth/{meeting_id}/answer/{answer_id}")  // 모임 답변 삭제 api (가입 거절)
    public String deleteAnswer(@PathVariable int meeting_id, @PathVariable int answer_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.deleteAnswer(user, meeting_id, answer_id);
        }
        return null;
    }

    @DeleteMapping("/auth/meeting/{meeting_id}/out/{user_id}")  // 모임 강퇴 api
    public String outUser(@PathVariable int meeting_id, @PathVariable int user_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.outUser(user, meeting_id, user_id);
        }
        return null;
    }

    @PatchMapping("/auth/meeting/{meeting_id}/notice")  // 모임 공지 생성 api
    public String createNotice(@PathVariable int meeting_id, @RequestBody PostNoticeReq postNoticeReq, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        meetingService.createNotice(meeting_id, principalDetails.getUser(), postNoticeReq);
        return "공지 생성 완료";
    }

    @DeleteMapping("/auth/meeting/{meeting_id}/notice")  // 모임 공지 삭제 api
    public String deleteNotice(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        meetingService.deleteNotice(meeting_id, principalDetails.getUser());
        return "공지 삭제 완료";
    }

    @GetMapping("/auth/meeting/{meeting_id}/check") // 모임 가입 여부 api
    public String check(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return meetingService.check(meeting_id, principalDetails.getUser());
    }
}