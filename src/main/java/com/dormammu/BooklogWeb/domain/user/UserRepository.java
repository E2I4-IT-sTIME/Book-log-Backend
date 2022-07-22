package com.dormammu.BooklogWeb.domain.user;

import com.dormammu.BooklogWeb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{
    User findByUsername(String username);
    User findByEmail(String email);

    User findById(int id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}