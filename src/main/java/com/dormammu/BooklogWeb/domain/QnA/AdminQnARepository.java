package com.dormammu.BooklogWeb.domain.QnA;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminQnARepository extends JpaRepository<AdminQnA, Integer> {
    AdminQnA findByMeetingId(int id);
    //List<AdminQnA> findByMeetingId2(int id);
    AdminQnA findById(int id);
}