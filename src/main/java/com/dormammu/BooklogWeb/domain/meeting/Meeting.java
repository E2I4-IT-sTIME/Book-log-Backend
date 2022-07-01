package com.dormammu.BooklogWeb.domain.meeting;

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

    @Column(nullable = false)
    private String name;

    private int max_num;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date meeting_date;

    @Column(nullable = false)
    private boolean onoff;

//    @ManyToMany
//    @JoinTable(name = "MEETING_USER",
//        joinColumns = @JoinColumn(name = "MEETING_ID"),
//        inverseJoinColumns = @JoinColumn(name = "USER_ID"))
//    private List<User> users = new ArrayList<User>();

    @OneToMany(mappedBy = "meeting")
    private List<MeetingUser> users = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;

}
