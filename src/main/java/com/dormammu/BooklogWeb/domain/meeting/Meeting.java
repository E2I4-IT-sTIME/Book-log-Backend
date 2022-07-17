package com.dormammu.BooklogWeb.domain.meeting;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
//import com.dormammu.BooklogWeb.domain.board.MeetingBoard;
import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meeting")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    @Lob
    @Column(nullable = false)
    private String info; // 모임 소개글

    @Lob
    private String image; // 대표 이미지

    private String ment; // 가입 안내 멘트


    @Column(nullable = false)
    private String name;

    private int max_num;

    private int cur_num;  // 현재 몇 명인지

    @Column(nullable = false)
    private boolean onoff;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<MeetingUser> users = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hashTag_id")
    private HashTag hashTag;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "adminQnA_id")
    private AdminQnA adminQnA;

    @Nullable
    private String notice;

}
