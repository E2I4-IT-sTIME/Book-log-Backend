package com.dormammu.BooklogWeb.domain.portfolio;

import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 생략 가능
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

    List<Portfolio> findByUser(User user);
}
