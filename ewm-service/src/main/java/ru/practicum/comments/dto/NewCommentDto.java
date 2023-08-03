package ru.practicum.comments.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {
    @NotEmpty
    @Size(min = 1, max = 280)
    private String text;
}
