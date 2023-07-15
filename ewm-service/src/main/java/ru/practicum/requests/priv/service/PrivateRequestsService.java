package ru.practicum.requests.priv.service;

import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestsService {
    List<ParticipationRequestDto> getRequestsByUserOtherEvents(int userId);

    ParticipationRequestDto createRequestsByUserOtherEvents(int userId, int eventId);

    ParticipationRequestDto cancelRequestsByUserOtherEvents(int userId, int requestId);

    List<ParticipationRequestDto> getRequestsByUser(int userId, int eventId);

    ConfirmedAndRejectedRequestsListsDto changeStatusRequestsByUser(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}

// TODO: 15.07.2023 refactor methods names 
