package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.portfolio.Portfolio;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.portfolio.PortfolioRepository;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PortfolioService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public List<Portfolio> myPortfolioList(User user) {
        System.out.println("portfolioService 들어옴");
        List<Portfolio> portfolioList = portfolioRepository.findByUser(user);
        //List<Portfolio> portfolios = user.getPortfolios(); <- 이 방법도 가능
        //System.out.println("포폴 : " + portfolios);
        System.out.println("레퍼지토리로 포폴 리스트 출력 : " + portfolioList);

        return portfolioList;
    }
}
