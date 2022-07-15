package com.dormammu.BooklogWeb.domain.hastag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Integer> {
    HashTag findByMeetingId(int id);
<<<<<<< HEAD
    HashTag findById(int id);
=======
>>>>>>> 71441cf (테이블 수정)
}
