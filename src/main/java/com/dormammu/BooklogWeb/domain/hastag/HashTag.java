package com.dormammu.BooklogWeb.domain.hastag;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hastag")
@ToString(exclude = "meeting")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String tag1;

    private String tag2;

    private String tag3;

    private String tag4;

    private String tag5;

//    @JsonIgnore
    @JsonBackReference
    @OneToOne(mappedBy = "hashTag", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meeting")
    private Meeting meeting;
}
