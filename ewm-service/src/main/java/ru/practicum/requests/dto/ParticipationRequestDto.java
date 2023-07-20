package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private EventUpdateRequestResultStatus status;
}
