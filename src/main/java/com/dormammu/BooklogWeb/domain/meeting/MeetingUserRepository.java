package com.dormammu.BooklogWeb.domain.meeting;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingUserRepository extends JpaRepository<MeetingUser, Integer> {

    MeetingUser findByUserIdAndMeetingId(int userId, int meetingId);

    List<MeetingUser> findByUserId(int userId);

    List<MeetingUser> findByMeetingId(int meetingId);
}
