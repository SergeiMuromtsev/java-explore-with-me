package ru.practicum.events.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.UserUpdateEventRequest;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventsController {
    private final PrivateEventsService privateEventsService;

    @GetMapping(path = "/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable(name = "userId") Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") int from,
                                               @RequestParam(name = "size", defaultValue = "10") int size) {
        log.debug("GET: {userId}/events", userId);
        return privateEventsService.getEventsByUser(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{userId}/events")
    public EventDto createEvents(@PathVariable(name = "userId") Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("POST: /users/{}/events", userId);
        return privateEventsService.createEvents(userId, newEventDto);
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    public EventDto getEventsByUserFullInfo(@PathVariable(name = "userId") Long userId,
                                                @PathVariable(name = "eventId") Long eventId) {
        log.debug("GET: users/{}/events/eventId", userId);
        return privateEventsService.getEventsByUserFullInfo(userId, eventId);
    }

    @PatchMapping(path = "{userId}/events/{eventId}")
    public EventDto changeEventsByUser(@PathVariable(name = "userId") Long userId,
                                           @PathVariable(name = "eventId") Long eventId,
                                           @RequestBody @Valid UserUpdateEventRequest userUpdateEventRequest) {
        log.debug("PATCH: users/{}/events/{eventId}", userId);
        return privateEventsService.changeEventsByUser(userId, eventId, userUpdateEventRequest);
    }
}
