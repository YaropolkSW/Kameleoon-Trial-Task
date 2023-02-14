package com.kameleoon.kameleoontrialtask.service;

import com.kameleoon.kameleoontrialtask.dao.RoleDAO;
import com.kameleoon.kameleoontrialtask.dao.UserDAO;
import com.kameleoon.kameleoontrialtask.dto.UserDTO;
import com.kameleoon.kameleoontrialtask.entity.Role;
import com.kameleoon.kameleoontrialtask.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND = "User not found";
    private final static String ANONYMOUS_USERNAME = "anonymousUser";

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
        final UserDAO userDAO,
        final RoleDAO roleDAO,
        final BCryptPasswordEncoder passwordEncoder
    ) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public String getUsernameFromSecurityContextHolder() {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(ANONYMOUS_USERNAME)) {
            return null;
        } else {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }

        return user;
    }

    public UserDTO findByUsername(final String username) {
        return UserDTO.of(userDAO.findByUsername(username));
    }

    public UserDTO findById(final int id) {
        return UserDTO.of(userDAO.findById(id).get());
    }

    public List<UserDTO> findAllUsers() {
        return userDAO.findAll().stream().map(UserDTO::of).collect(Collectors.toList());
    }

    public boolean saveNewUser(final UserDTO userDTO) {
        if (userDAO.findByUsername(userDTO.getUsername()) != null) {
            return false;
        }

        final User user = User.builder()
            .username(userDTO.getUsername())
            .email(userDTO.getEmail())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .dateOfCreation(LocalDate.now())
            .roles(getUserRole())
            .build();

        userDAO.save(user);

        return true;
    }

    private List<Role> getUserRole() {
        final List<Role> userRole = new ArrayList<>();

        userRole.add(roleDAO.findById(2));

        return userRole;
    }
}
