package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private EventUpdateRequestResultStatus status;
}

// TODO: 15.07.2023 Refactor all that sh names