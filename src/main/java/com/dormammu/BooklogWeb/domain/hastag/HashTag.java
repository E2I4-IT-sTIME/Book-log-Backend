package com.dormammu.BooklogWeb.domain.hastag;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hastag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tag1;

    private String tag2;

    private String tag3;

    private String tag4;

    private String tag5;

    @OneToOne
    @JoinColumn(name = "meeting")
    private Meeting meeting;

}
