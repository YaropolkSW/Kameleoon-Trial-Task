package com.kameleoon.kameleoontrialtask.dao;

import com.kameleoon.kameleoontrialtask.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer> {
    Role findById(final int id);
}
