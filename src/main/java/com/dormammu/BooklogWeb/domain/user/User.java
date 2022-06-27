package com.dormammu.BooklogWeb.domain.user;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    //@Column(nullable = false)
    private String email;

    private String imgHome;
    private String imgPath;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;

    private String job;

    private String area;

    private boolean active;

    private String roles;  // ROLE_USER, ROLE_ADMIN

    @CreationTimestamp
    private Timestamp createDate;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<Portfolio> portfolios;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    private List<Review> reviews;

    @ManyToMany(mappedBy = "users")
    private List<Meeting> meetings;

}