package com.dormammu.BooklogWeb.domain.QnA;

import com.dormammu.BooklogWeb.domain.meeting.Meeting;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adminQnA")
public class AdminQnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    private String Q1;

    @Lob
    private String Q2;

    @Lob
    private String Q3;
    @Lob
    private String Q4;
    @Lob
    private String Q5;

    @OneToMany(mappedBy = "adminQnA", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserQnA> userQnA;

    @OneToOne(mappedBy = "adminQnA", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Meeting meeting;
}