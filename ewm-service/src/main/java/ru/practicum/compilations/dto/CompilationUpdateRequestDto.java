package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
@Builder
public class CompilationUpdateRequestDto {
    @UniqueElements
    private List<Long> events;
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
