package com.dormammu.BooklogWeb.domain.QnA;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_QnA")
public class UserQnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    private String A1;

    @Lob
    private String A2;

    @Lob
    private String A3;

    @ManyToOne
    private AdminQnA adminQnA;

}
