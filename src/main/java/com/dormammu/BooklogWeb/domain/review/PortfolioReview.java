package com.dormammu.BooklogWeb.domain.review;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "portfolio_review")
public class PortfolioReview {

    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;
}
