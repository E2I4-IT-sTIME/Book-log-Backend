package com.dormammu.BooklogWeb.service;

import com.dormammu.BooklogWeb.domain.user.User;
import com.dormammu.BooklogWeb.domain.user.UserRepository;
import com.dormammu.BooklogWeb.dto.JoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

//    public void joinUser(JoinRequestDto joinRequestDto, MultipartFile multipartFile) throws IOException {
//        User user = new User();
//        user.setUsername(joinRequestDto.getUsername());
//        user.setPassword(joinRequestDto.getPassword());
//        user.setEmail(joinRequestDto.getEmail());
//        user.setImgHome(joinRequestDto.getImgHome());
//        user.setImgPath(joinRequestDto.getImgPath());
//        user.setBirthday(joinRequestDto.getBirthday());
//        user.setJob(joinRequestDto.getJob());
//        user.setArea(joinRequestDto.getArea());
//        user.setActive(joinRequestDto.isActive());
//        user.setRoles(joinRequestDto.getRoles());
//        userRepository.save(user);
//
//        String r = s3Uploader.uploadProfile(user.getId(), multipartFile, "static");
//        System.out.println(r);
//    }

    public void joinUser(User user) {

        userRepository.save(user);
    }

    public void updateUser(int id, String username, Date birthday, String job, User loginUser) {
        User user = userRepository.findById(id);
        if (loginUser.getId() == user.getId()) {
            user.setUsername(username);
            user.setBirthday(birthday);
            user.setJob(job);
            userRepository.save(user);
        }
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    /* 회원가입 시, 유효성 체크 */
    @Transactional //(readOnly = true)
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
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
}
