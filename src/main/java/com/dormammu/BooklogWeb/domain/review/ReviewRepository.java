package com.dormammu.BooklogWeb.domain.review;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 생략 가능
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByUser(User user);
    List<Review> findByUserId(int userId);

    Review findById(int id);
}
