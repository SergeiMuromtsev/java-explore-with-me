package ru.practicum.events.publ.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.OpenEventRequests;
import ru.practicum.events.publ.service.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PublicEventsController {
    @Autowired
    private PublicEventsService publicEventsService;

    @GetMapping(path = "/events")
    public List<EventShortDto> getEvents(@RequestParam(name = "text", required = false) String text,
                                         @RequestParam(name = "categories", required = false) List<Integer> categories,
                                         @RequestParam(name = "paid", required = false) String paid,
                                         @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                         @RequestParam(name = "onlyAvailable", required = false) String onlyAvailable,
                                         @RequestParam(name = "sort", required = false) String sort,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.debug("LogMessages.TRY_PUBLIC_GET_EVENT.label");
        return publicEventsService
                .getEvents(OpenEventRequests.of(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size), request);
    }

    @GetMapping(path = "/events/{eventId}")
    public EventDto getEventsById(@PathVariable(name = "eventId") int eventId, HttpServletRequest request) {
        log.debug("LogMessages.TRY_PUBLIC_GET_EVENT_ID.label, eventId");
        return publicEventsService.getEventsById(eventId, request);
    }
}