package com.dormammu.BooklogWeb.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Comment findById(int comment_id);
    List<Comment> findByMeetingId(int meeting_id);
}
