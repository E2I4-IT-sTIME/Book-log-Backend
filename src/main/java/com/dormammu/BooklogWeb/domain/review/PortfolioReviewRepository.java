package com.dormammu.BooklogWeb.domain.review;

import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 생략 가능
public interface PortfolioReviewRepository extends JpaRepository<PortfolioReview, Integer> {
    void deleteByPortfolioId(int portfolio_id);
    List<PortfolioReview> findByPortfolioId(int portfolio_id);
}
