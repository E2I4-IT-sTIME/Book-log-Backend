package com.dormammu.BooklogWeb.repository;

import com.dormammu.BooklogWeb.model.Portfolio;
import com.dormammu.BooklogWeb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 생략 가능
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findByUserInfo(User userInfo);
}
