package com.dormammu.BooklogWeb.domain.review;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
@ToString(exclude = "portfolio")
public class Review {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String content;

    private String book_name;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    @Nullable
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private Timestamp createDate;
}
