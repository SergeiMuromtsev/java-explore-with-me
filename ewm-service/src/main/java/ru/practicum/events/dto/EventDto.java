package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.model.EventStatus;
import ru.practicum.locations.model.Location;
import ru.practicum.users.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class EventDto {
    private Integer id;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests;
    private String createdOn;
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private boolean paid;
    private int participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private EventStatus state;
    @NotBlank
    private String title;
    private Long views;
}