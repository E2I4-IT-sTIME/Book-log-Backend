package com.dormammu.BooklogWeb.domain.meeting;

import com.dormammu.BooklogWeb.domain.QnA.AdminQnA;
import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingDateRepository extends JpaRepository<MeetingDate, Integer> {
    List<MeetingDate> findByMeetingId(int meeting_id);
}