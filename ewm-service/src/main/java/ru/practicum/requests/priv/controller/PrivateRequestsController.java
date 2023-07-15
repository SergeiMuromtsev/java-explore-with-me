package ru.practicum.requests.priv.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.priv.service.PrivateRequestsService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestsController {
    @Autowired
    private PrivateRequestsService service;

    @GetMapping(path = "{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserOtherEvents(@PathVariable(name = "userId") int userId) {
        log.debug("GET: /users/{}/requests", userId);
        return service.getRequestsByUserOtherEvents(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "{userId}/requests")
    public ParticipationRequestDto createRequestsByUserOtherEvents(@PathVariable(name = "userId") int userId,
                                                                   @RequestParam(name = "eventId") int eventId) {
        log.debug("POST: /users/{}/requests", userId);
        return service.createRequestsByUserOtherEvents(userId, eventId);
    }

    @PatchMapping(path = "{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestsByUserOtherEvents(@PathVariable(name = "userId") int userId,
                                                                   @PathVariable(name = "requestId") int requestId) {
        log.debug("PATCH: users/{}/requests/{requestId}/cancel", userId);
        return service.cancelRequestsByUserOtherEvents(userId, requestId);
    }

    @GetMapping(path = "{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable(name = "userId") int userId,
                                                           @PathVariable(name = "eventId") int eventId) {
        log.debug("GET: users/{}/events/{}/requests", userId, eventId);
        return service.getRequestsByUser(userId, eventId);
    }

    @PatchMapping(path = "{userId}/events/{eventId}/requests")
    public ConfirmedAndRejectedRequestsListsDto changeStatusRequestsByUser(@PathVariable(name = "userId") int userId,
                                                                           @PathVariable(name = "eventId") int eventId,
                                                                           @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("PATCH: users/{}/events/{}/requests", userId, eventId);
        return service.changeStatusRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
