package com.dormammu.BooklogWeb.controller;

import com.dormammu.BooklogWeb.config.auth.PrincipalDetails;
import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.meeting.MeetingRepository;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.*;
import com.dormammu.BooklogWeb.service.MeetingService;
import com.dormammu.BooklogWeb.service.S3Uploader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"모임관련 API"})  // Swagger 최상단 Controller 명칭
public class MeetingController {

    private final MeetingService meetingService;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final S3Uploader s3Uploader;

    @PostMapping("/auth/meeting")  // 모임 생성 API(+이미지)
    @ApiOperation(value = "모임 생성 API(+이미지)", notes = "모임을 새로 생성하는 API입니다. 이미지는 multipartFile로!")  // Swagger에 사용하는 API에 대한 간단 설명
    @ApiImplicitParams(
            {@ApiImplicitParam(name= "image", value = "모임 이미지"),
                    @ApiImplicitParam(name= "name", value = "모임 이름"),
                    @ApiImplicitParam(name= "info", value = "모임 소개글"),
                    @ApiImplicitParam(name= "ment", value = "모임가입 멘트"),
                    @ApiImplicitParam(name= "max_num", value = "최대 인원"),
                    @ApiImplicitParam(name= "onoff", value = "온/오프라인"),
                    @ApiImplicitParam(name= "questions", value = "질문"),
                    @ApiImplicitParam(name= "hashtags", value = "해시태그")
            }
            )   // Swagger에 사용하는 파라미터에 대해 설명
    public String createMeeting(Authentication authentication,
                                @RequestPart(value = "image") MultipartFile multipartFile, @RequestParam("name") String name, @RequestParam("info") String info,
                                @RequestParam("ment") String ment, @RequestParam("max_num") String max_num, @RequestParam("onoff") String onoff,
                                @RequestParam("questions")List<String> questions, @RequestParam("hashtags")List<String> hashtags) throws IOException {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());
        return meetingService.createMeeting(user, multipartFile, name, info, ment, max_num, onoff, questions, hashtags);
    }

    @ApiOperation(value = "모임 조회", notes = "모임 리스트 조회 API")
    @GetMapping("/meetings")  // 모임 리스트 조회 API
    public List<GetMeetingRes> meetingList(){
        return meetingService.meetingList();
    }

    @ApiOperation(value = "모임 개별 조회", notes = "모임 개별 조회 API, 로그인 여부와 상관없이 다 가능함")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @GetMapping("/meetings/{meeting_id}")  // 모임 개별 조회 API -> 로그인x/로그인o 전부 열려있어서 로그인o 사용자여도 토큰 안옴
    public GetOneMeetingRes oneMeeting(@PathVariable int meeting_id){

        return meetingService.oneMeeting(meeting_id);
    }

    @ApiOperation(value = "내 모임 리스트 조회", notes = "내 모임 조회 API")
    @ApiImplicitParam(name = "id", value = "유저 id값")
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

    @ApiOperation(value = "모임 가입 신청", notes = "모임 가입 신청 API")
    @ApiImplicitParam(name = "id", value = "모임 id값")
    @PostMapping("/auth/meetings/{id}")  // 모임 가입 신청 API
    public String addMeeting(@RequestBody PostAnswerReq postAnswerReq, @PathVariable int id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Meeting meeting = meetingRepository.findById(id);
        User user = userRepository.findById(principalDetails.getUser().getId());

        return meetingService.addMeeting(user, meeting, postAnswerReq);

    }

    @ApiOperation(value = "모임 탈퇴", notes = "모임 탈퇴 API")
    @ApiImplicitParam(name = "id", value = "모임 id값")
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

    @ApiOperation(value = "모임 정보 수정", notes = "모임 수정 API")
    @ApiImplicitParam(name = "id", value = "모임 id값")
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

    @ApiOperation(value = "모임 삭제", notes = "모임 삭제 API")
    @ApiImplicitParam(name = "id", value = "모임 id값")
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

    @ApiOperation(value = "모임 질문 조회하기", notes = "모임 질문 조회 API")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @GetMapping("/auth/{meeting_id}/question")  // 모임 질문 조회 API
    public MeetingRes questionList(@PathVariable int meeting_id, Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        return meetingService.questionList(meeting_id);
    }

    @ApiOperation(value = "모임 답변 조회", notes = "모임 답변 전체 조회 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @GetMapping("/auth/meetings/{meeting_id}/answers")  // 모임 답변 전체 조회 api
    public List<GetAnswerRes> answerList(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        return meetingService.answerList(meeting_id, principalDetails.getUser());
    }


    @ApiOperation(value = "안쓰는 api", notes = "모임 답변 개별 조회 api -> 사용 x 예정")
    @GetMapping("/auth/meeting/{meeting_id}/answers/{user_id}")  // 모임 답변 개별 조회 api -> 사용 x 예정
    public GetAnswerRes oneAnswer(@PathVariable int meeting_id, Authentication authentication, @PathVariable int user_id){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(user_id);


        return meetingService.oneAnswer(user,meeting_id);

    }

    @ApiOperation(value = "모임 가입 수락", notes = "모임 답변 수락 api (가입 수락)")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "meeting_id", value = "모임 id값"),
                    @ApiImplicitParam(name = "answer_id", value = "답변 id값")
            })
    @PostMapping("/auth/{meeting_id}/answer/{answer_id}")  // 모임 답변 수락 api (가입 수락)
    public String acceptAnswer(@PathVariable int meeting_id, @PathVariable int answer_id, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.acceptAnswer(user, meeting_id, answer_id);
        }
        return "모임장만 수락할 수 있습니다.";
    }

    @ApiOperation(value = "모임 가입 거절", notes = "모임 답변 삭제 api (가입 거절)")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "meeting_id", value = "모임 id값"),
                    @ApiImplicitParam(name = "answer_id", value = "답변 id값")
            })
    @DeleteMapping("/auth/{meeting_id}/answer/{answer_id}")  // 모임 답변 삭제 api (가입 거절)
    public String deleteAnswer(@PathVariable int meeting_id, @PathVariable int answer_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.deleteAnswer(user, meeting_id, answer_id);
        }
        return null;
    }

    @ApiOperation(value = "모임 강퇴", notes = "모임 강퇴 api")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "meeting_id", value = "모임 id값"),
                    @ApiImplicitParam(name = "user_id", value = "유저 id값")
            })
    @DeleteMapping("/auth/meeting/{meeting_id}/out/{user_id}")  // 모임 강퇴 api
    public String outUser(@PathVariable int meeting_id, @PathVariable int user_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = userRepository.findById(principalDetails.getUser().getId());

        if (user.getId() == principalDetails.getUser().getId()){
            return meetingService.outUser(user, meeting_id, user_id);
        }
        return null;
    }

    @ApiOperation(value = "독서 모임 상세페이지", notes = "독서 모임 상세페이지 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @GetMapping("/auth/meeting/{meeting_id}")  // 독서 모임 상세페이지 api
    public GetMeetingDesRes meetingMain(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return meetingService.meetingMain(principalDetails.getUser(), meeting_id);
    }

    @ApiOperation(value = "모임 공지 띄우기", notes = "모임 공지 생성 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @PatchMapping("/auth/meeting/{meeting_id}/notice")  // 모임 공지 생성 api
    public String createNotice(@PathVariable int meeting_id, @RequestBody PostNoticeReq postNoticeReq, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        meetingService.createNotice(meeting_id, principalDetails.getUser(), postNoticeReq);
        return "공지 생성 완료";
    }

    @ApiOperation(value = "모임 공지 삭제", notes = "모임 공지 삭제 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @DeleteMapping("/auth/meeting/{meeting_id}/notice")  // 모임 공지 삭제 api
    public String deleteNotice(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        meetingService.deleteNotice(meeting_id, principalDetails.getUser());
        return "공지 삭제 완료";
    }

    @ApiOperation(value = "모임 가입 여부 확인", notes = "모임 가입 여부 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @GetMapping("/auth/meeting/{meeting_id}/check") // 모임 가입 여부 api
    public int check(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return meetingService.check(meeting_id, principalDetails.getUser());
    }

    @ApiOperation(value = "출석 완료", notes = "출석 완료 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @PostMapping("/auth/meeting/{meeting_id}/attendance")  // 출석 완료 api
    public String attendance(@PathVariable int meeting_id, @RequestParam("date") String date, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        return meetingService.attendance(meeting_id, date, principalDetails.getUser());

    }

    @ApiOperation(value = "모임 공지 조회", notes = "모임 공지 조회 api")
    @ApiImplicitParam(name = "meeting_id", value = "모임 id값")
    @GetMapping("/auth/meeting/{meeting_id}/notice")  // 모임 공지 조회 api
    public GetNoticeRes notice(@PathVariable int meeting_id, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return meetingService.notice(meeting_id);
    }

    @ApiOperation(value = "독서 모임명 검색", notes = "독서 모임명으로 검색 api")
    @GetMapping("/auth/meeting/searchName")  // 독서 모임명으로 검색 api
    public List<GetMeetingRes> searchMeeting(@RequestParam("name") String name, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return meetingService.searchMeeting(name);
    }

    // 이거 아직X
    @ApiOperation(value = "카테고리명 검색", notes = "카테고리명으로 검색 api")
    @GetMapping("/auth/meeting/searchCategory")  // 카테고리명으로 검색 api
    public List<GetMeetingRes> searchCategory(@RequestParam("category") String category, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return meetingService.searchCategory(category);
    }
}