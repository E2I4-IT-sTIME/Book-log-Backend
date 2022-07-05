package com.dormammu.BooklogWeb.domain.meeting;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    private String area;  // 오프라인- 활동지역

    private String ment; // 가입 안내 멘트


    @Column(nullable = false)
    private String name;

    private int max_num;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date meeting_date;

    @Column(nullable = false)
    private boolean onoff;

    @OneToMany(mappedBy = "meeting", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MeetingUser> users = new ArrayList<>();


}
