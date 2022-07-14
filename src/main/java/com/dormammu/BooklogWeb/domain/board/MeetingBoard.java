//package com.dormammu.BooklogWeb.domain.board;
//
//import com.dormammu.BooklogWeb.domain.comment.Comment;
//import com.dormammu.BooklogWeb.domain.meeting.Meeting;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "meetingBoard")
//public class MeetingBoard {
//
//    // 게시판- 댓글 1:N관계
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @OneToMany(mappedBy = "meetingBoard", cascade = CascadeType.ALL)
//    private List<Comment> comment;
//
//    @OneToOne(mappedBy = "meetingBoard")
//    private Meeting meeting;
//
//    private String board_title;
//
//    private String board_content;
//
//}