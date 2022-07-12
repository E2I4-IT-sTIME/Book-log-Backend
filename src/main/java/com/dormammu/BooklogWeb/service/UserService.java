package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.review.Review;
import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.GetCommunityRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void deleteUser(int id) {
        User user = userRepository.findById(id);
        user.setActive(false);
        userRepository.save(user);
    }


}
