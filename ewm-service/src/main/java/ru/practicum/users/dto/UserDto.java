package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String name;
}
