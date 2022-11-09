package com.dormammu.BooklogWeb.domain.portfolio;

import com.dormammu.BooklogWeb.domain.review.PortfolioReview;
import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

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

    private String image;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PortfolioReview> reviews;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

//    @DateTimeFormat(pattern = "yyyy-mm-dd")
//    private LocalDate createDate;
    @CreationTimestamp
    private Timestamp createDate;
}
