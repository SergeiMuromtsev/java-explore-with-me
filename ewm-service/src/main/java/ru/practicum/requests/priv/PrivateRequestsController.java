package ru.practicum.requests.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ConfirmedAndRejectedRequestsListsDto;
import ru.practicum.requests.dto.EventStatusUpdateRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestsController {
    private final PrivateRequestsService service;

    @GetMapping(path = "{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserSomeonesEvent(@PathVariable(name = "userId") Long userId) {
        log.debug("GET: /users/{}/requests", userId);
        return service.getRequestsByUserSomeonesEvent(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "{userId}/requests")
    public ParticipationRequestDto createRequestsByUserSomeonesEvent(@PathVariable(name = "userId") Long userId,
                                                                   @RequestParam(name = "eventId") Long eventId) {
        log.debug("POST: /users/{}/requests", userId);
        return service.createRequestsByUserSomeonesEvent(userId, eventId);
    }

    @PatchMapping(path = "{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestsByUserSomeonesEvent(@PathVariable(name = "userId") Long userId,
                                                                   @PathVariable(name = "requestId") Long requestId) {
        log.debug("PATCH: users/{}/requests/{requestId}/cancel", userId);
        return service.cancelRequestsByUserSomeonesEvent(userId, requestId);
    }

    @GetMapping(path = "{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable(name = "userId") Long userId,
                                                           @PathVariable(name = "eventId") Long eventId) {
        log.debug("GET: users/{}/events/{}/requests", userId, eventId);
        return service.getRequestsByUser(userId, eventId);
    }

    @PatchMapping(path = "{userId}/events/{eventId}/requests")
    public ConfirmedAndRejectedRequestsListsDto changeStatusRequestsByUser(@PathVariable(name = "userId") Long userId,
                                                                           @PathVariable(name = "eventId") Long eventId,
                                                                           @RequestBody EventStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("PATCH: users/{}/events/{}/requests", userId, eventId);
        return service.changeStatusRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
