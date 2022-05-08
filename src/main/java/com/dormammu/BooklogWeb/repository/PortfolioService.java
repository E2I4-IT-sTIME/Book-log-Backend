package com.dormammu.BooklogWeb.repository;

import com.dormammu.BooklogWeb.model.Portfolio;
import com.dormammu.BooklogWeb.model.User;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final UserRepository userRepository;

    @Transactional
    public List<Portfolio> portfolList(User user) {
        System.out.println("portfolioService 들어옴");
        List<Portfolio> portfolios = user.getPortfolios();
        System.out.println("포폴 : " + portfolios);
        return portfolios;
    }
}
