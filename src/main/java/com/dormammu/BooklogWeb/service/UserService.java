package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.GetUserInfoRes;
import com.dormammu.BooklogWeb.dto.GetUserRes;
import com.dormammu.BooklogWeb.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public void updateUser(int id, String username, User loginUser) throws IOException {

        User user = userRepository.findById(id);
        if (loginUser.getId() == user.getId()) {
            user.setUsername(username);
            userRepository.save(user);
        }
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public GetUserRes myPage(int id, User loginUser) {

        if (loginUser.getId() == id) {
            User user = userRepository.findById(id);
            GetUserRes getUserRes = new GetUserRes();

            getUserRes.setId(user.getId());
            getUserRes.setImage(user.getImgPath());
            getUserRes.setUsername(user.getUsername());
            getUserRes.setEmail(user.getEmail());

            return getUserRes;
        }
        return null;
    }

    // 이름, 프로필 사진 제공
    @Transactional
    public GetUserInfoRes userInfo(User user) {
        try {
            GetUserInfoRes getUserInfoRes = GetUserInfoRes.builder()
                    .name(user.getUsername())
                    .profile(user.getImgPath()).build();
            return getUserInfoRes;
        } catch (Exception e) {
            return null;
        }
    }

    /* 이메일 중복 여부 확인 */
    @Transactional
    public Boolean checkEmailDuplication(String email) {
        boolean emailDuplicate = userRepository.existsByEmail(email);
        if (emailDuplicate) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        } else {
            return true;
        }
    }

    /* id로 User 찾기 */
    @Transactional
    public User findUser(int user_id) {
        try {
            User user = userRepository.findById(user_id);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}
