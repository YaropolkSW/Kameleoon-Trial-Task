package com.kameleoon.kameleoontrialtask.response;

import com.kameleoon.kameleoontrialtask.dto.DTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Response implements DTO {
    private String message;
}
