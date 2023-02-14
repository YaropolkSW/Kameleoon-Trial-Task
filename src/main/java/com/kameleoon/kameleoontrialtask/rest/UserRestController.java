package com.kameleoon.kameleoontrialtask.rest;

import com.kameleoon.kameleoontrialtask.dto.DTO;
import com.kameleoon.kameleoontrialtask.dto.UserDTO;
import com.kameleoon.kameleoontrialtask.response.Response;
import com.kameleoon.kameleoontrialtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final static String NO_SUCH_USER = "No such user!";
    private final static String CANT_SEE_PASSWORD = "You can't see password of another user!";

    private final UserService userService;

    @Autowired
    public UserRestController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public DTO registerNewUser(@RequestBody final UserDTO user) {
        user.setDateOfCreation(LocalDate.now());

        userService.saveNewUser(user);

        return user;
    }

    @GetMapping("/info/{userId}")
    public DTO getUserInfo(@PathVariable final int userId) {
        List<Integer> userIds = userService.findAllUsers().stream().map(UserDTO::getId).collect(Collectors.toList());

        if (userIds.contains(userId)) {
            final UserDTO user = userService.findById(userId);

            if (user.getUsername().equals(userService.getUsernameFromSecurityContextHolder())) {
                return user;
            } else {
                user.setPassword(CANT_SEE_PASSWORD);

                return user;
            }
        } else {
            return new Response(NO_SUCH_USER);
        }
    }
}
