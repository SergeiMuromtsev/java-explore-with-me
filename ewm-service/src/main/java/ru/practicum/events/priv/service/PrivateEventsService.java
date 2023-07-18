package ru.practicum.events.priv.service;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UserUpdateEventRequest;
import ru.practicum.events.model.Event;

import java.util.List;

public interface PrivateEventsService {
    List<EventShortDto> getEventsByUser(int userId, int from, int size);

    EventDto createEvents(Integer userId, NewEventDto newEventDto);

    EventDto getEventsByUserFullInfo(int userId, int eventId);

    EventDto changeEventsByUser(int userId, int eventId, UserUpdateEventRequest userUpdateEventRequest);

    Event getEventById(int eventId);
}
