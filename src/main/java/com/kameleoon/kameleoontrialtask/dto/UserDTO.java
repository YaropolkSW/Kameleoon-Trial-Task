package com.kameleoon.kameleoontrialtask.dto;

import com.kameleoon.kameleoontrialtask.entity.User;
import lombok.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO implements DTO {
    private int id;

    @Size(min = 2, message = "Username must be at least 2 characters long")
    private String username;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Incorrect email. Example - some-email@gmail.com")
    private String email;

    @Size(min = 6, message = "Password must be at less 6 characters long")
    private String password;

    private LocalDate dateOfCreation;

    public static UserDTO of(final User user) {
        return UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .password(user.getPassword())
            .dateOfCreation(user.getDateOfCreation())
            .build();
    }
}
