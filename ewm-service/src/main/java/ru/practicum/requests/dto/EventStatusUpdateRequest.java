package ru.practicum.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.requests.model.EventUpdateRequestResultStatus;

import java.util.List;

@Data
@Builder
public class EventStatusUpdateRequest {
    private List<Long> requestIds;
    private EventUpdateRequestResultStatus status;
}
