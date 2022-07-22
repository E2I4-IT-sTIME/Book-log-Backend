package com.dormammu.BooklogWeb.domain.meeting;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.hastag.HashTag;
import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    List<Meeting> findByUsers(User user);
    List<Meeting> findByUserId(int userId);
    Meeting findById(int id);
    //List<Meeting> findAll();
    AdminQnA findByAdminQnAId(int adminQnAId);
}