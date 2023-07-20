package ru.practicum.events.priv;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UserUpdateEventRequest;
import ru.practicum.events.model.Event;

import java.util.List;

public interface PrivateEventsService {
    List<EventShortDto> getEventsByUser(Long userId, int from, int size);

    EventDto createEvents(Long userId, NewEventDto newEventDto);

    EventDto getEventsByUserFullInfo(Long userId, Long eventId);

    EventDto changeEventsByUser(Long userId, Long eventId, UserUpdateEventRequest userUpdateEventRequest);

    Event getEventById(Long eventId);
}
