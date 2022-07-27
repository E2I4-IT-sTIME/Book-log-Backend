package com.dormammu.BooklogWeb.domain.QnA;

import com.dormammu.BooklogWeb.domain.meeting.MeetingUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQnARepository extends JpaRepository<UserQnA, Integer> {

    List<UserQnA> findByAdminQnAId(int id);
    UserQnA findByUserIdAndAdminQnAId(int userId, int adminQnAId);

    List<UserQnA> findByAdminQnAIdAndUserId(int adminQnAId, int userId);

    UserQnA findById(int id);
}
