package ru.practicum.requests.priv;

import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.dto.EventStatusUpdateRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestsService {
    List<ParticipationRequestDto> getRequestsByUserSomeonesEvent(Long userId);

    ParticipationRequestDto createRequestsByUserSomeonesEvent(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestsByUserSomeonesEvent(Long userId, Long requestId);

    List<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId);

    ConfirmedAndRejectedRequestsListsDto changeStatusRequestsByUser(Long userId, Long eventId, EventStatusUpdateRequest eventRequestStatusUpdateRequest);
}
