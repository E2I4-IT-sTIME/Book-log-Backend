package com.dormammu.BooklogWeb.domain.user;

import com.dormammu.BooklogWeb.domain.comment.Comment;
import com.dormammu.BooklogWeb.domain.meeting.MeetingUser;
import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "portfolios")
@Table(name = "user")
public class User {

    @Id  // Primary key
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String nickname = "닉네임"; // 나중에 db create해서 이 필드 지우기

    @Column(nullable = false, unique = true)
    private String email;

    private String imgHome;
    private String imgPath;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    private String job;

    private String area;

    private boolean active = true;

    private String roles;  // ROLE_USER, ROLE_ADMIN

    @CreationTimestamp
    private Timestamp createDate;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<Portfolio> portfolios;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<Review> reviews;

//    @ManyToMany(mappedBy = "users")
//    private List<Meeting> meetings;

    @OneToMany(mappedBy = "user")
    private List<MeetingUser> meetingUsers = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments;

}