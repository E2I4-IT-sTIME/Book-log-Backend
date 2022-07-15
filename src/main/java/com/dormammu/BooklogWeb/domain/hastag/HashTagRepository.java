package com.dormammu.BooklogWeb.domain.hastag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Integer> {
    HashTag findByMeetingId(int id);
    HashTag findById(int id);
}
