//package com.dormammu.BooklogWeb.domain.comment;
//
//import com.dormammu.BooklogWeb.domain.board.MeetingBoard;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "comment")
//public class Comment {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private int userId;  // 댓글 작성자
//
//    private String content;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private MeetingBoard meetingBoard;
//
//}
