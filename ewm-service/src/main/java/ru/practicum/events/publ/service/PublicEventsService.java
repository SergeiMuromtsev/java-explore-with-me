package ru.practicum.events.publ.service;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.OpenEventRequests;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventsService {
    List<EventShortDto> getEvents(OpenEventRequests requests, HttpServletRequest request);

    EventDto getEventsById(int eventId, HttpServletRequest request);
}
