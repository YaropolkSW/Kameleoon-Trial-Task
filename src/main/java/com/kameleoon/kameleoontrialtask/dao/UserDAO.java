package com.kameleoon.kameleoontrialtask.dao;

import com.kameleoon.kameleoontrialtask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(final String username);
}
