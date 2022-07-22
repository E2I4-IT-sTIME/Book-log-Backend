package com.dormammu.BooklogWeb.domain.QnA;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQnARepository extends JpaRepository<UserQnA, Integer> {

    List<UserQnA> findByAdminQnAId(int id);
    UserQnA findByUserIdAndAdminQnAId(int userId, int adminQnAId);

    UserQnA findById(int id);
}
