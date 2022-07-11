package com.dormammu.BooklogWeb.domain.portfolio;

import com.dormammu.BooklogWeb.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "portfolio")
public class Portfolio {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate createDate;
}
