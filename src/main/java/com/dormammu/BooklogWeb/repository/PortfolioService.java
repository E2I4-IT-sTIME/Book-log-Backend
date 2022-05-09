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
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public List<Portfolio> portfolList(User user) {
        System.out.println("portfolioService 들어옴");
        List<Portfolio> portfolioList = portfolioRepository.findByUserInfo(user);
        List<Portfolio> portfolios = user.getPortfolios();
        System.out.println("포폴 : " + portfolios);
        System.out.println("레퍼지토리로 포폴 리스트 출력 : " + portfolioList);

        return portfolioList;
    }
}
