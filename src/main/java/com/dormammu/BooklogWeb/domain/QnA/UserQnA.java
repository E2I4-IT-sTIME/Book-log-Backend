package com.dormammu.BooklogWeb.domain.QnA;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_QnA")
@ToString(exclude = "adminQnA")
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

    @Lob
    private String A4;

    @Lob
    private String A5;

    private int userId;

    @ManyToOne
    @JoinColumn(name = "adminQnA_id")
    private AdminQnA adminQnA;

}
