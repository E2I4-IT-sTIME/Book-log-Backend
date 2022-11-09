package com.dormammu.BooklogWeb.domain.review;

import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 생략 가능
public interface PortfolioReviewRepository extends JpaRepository<PortfolioReview, Integer> {
    PortfolioReview findByPofolioIdAndReviewId(int portfolio_id, int review_id);
    List<PortfolioReview> findByPofolioId(int portfolio_id);
    Boolean existsByPofolioIdAndReviewId(int portfolio_id, int review_id);

    void deleteByPortfolioId(int portfolio_id);
}
