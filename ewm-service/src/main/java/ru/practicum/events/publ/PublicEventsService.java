package ru.practicum.events.publ;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.PublicEventRequests;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventsService {
    List<EventShortDto> getEvents(PublicEventRequests requests, HttpServletRequest request);

    EventDto getEventsById(Long eventId, HttpServletRequest request);
}
