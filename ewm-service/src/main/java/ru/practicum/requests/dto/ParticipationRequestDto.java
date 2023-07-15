package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;

@Data
@Builder
public class ParticipationRequestDto {
    private Integer id;
    private String created;
    private int event;
    private int requester;
    private EventUpdateRequestResultStatus status;
}
