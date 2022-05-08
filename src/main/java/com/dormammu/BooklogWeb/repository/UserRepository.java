package com.dormammu.BooklogWeb.repository;

import com.dormammu.BooklogWeb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}