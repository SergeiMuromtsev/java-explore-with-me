package ru.practicum.events.admin.service;

import ru.practicum.events.dto.AdminEventRequests;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.AdminUpdateEventRequest;

import java.util.List;

public interface AdminEventsService {
    List<EventDto> findEvents(AdminEventRequests requests);

    EventDto changeEvents(Integer eventId, AdminUpdateEventRequest adminUpdateEventRequest);
}
