package ru.practicum.events.publ.service;

import com.google.gson.Gson;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.PublicEventRequests;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.model.EventsSort;
import ru.practicum.events.model.QEvent;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidateException;
import ru.practicum.statclient.StatClient;
import ru.practicum.statdto.StatDto;
import ru.practicum.statdto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.events.mapper.EventsMapper.toEventDto;
import static ru.practicum.events.mapper.EventsMapper.toListEventShortDto;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicEventsServiceImpl implements PublicEventsService {
    private final EventsRepository repository;
    private final StatClient statClient;
    private final Gson gson = new Gson();

    @Override
    public List<EventShortDto> getEvents(PublicEventRequests requests, HttpServletRequest request) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (requests.getText() != null) {
            conditions.add(event.annotation.containsIgnoreCase(requests.getText())
                    .or(event.description.containsIgnoreCase(requests.getText())));
        }

        if (requests.hasCategories()) {
            for (Integer id : requests.getCategories()) {
                conditions.add(event.category.id.eq(id));
            }
        }

        if (requests.getPaid() != null) {
            conditions.add(event.paid.eq(requests.getPaid()));
        }

        if (requests.getRangeStart() != null && requests.getRangeEnd() != null) {
            if (requests.getRangeStart().isAfter(requests.getRangeEnd())) {
                throw new ValidateException("wrong time");
            } else {
                conditions.add(event.eventDate.between(requests.getRangeStart(), requests.getRangeEnd()));
            }
        }

        if (requests.getOnlyAvailable() != null) {
            conditions.add(event.confirmedRequests.loe(event.participantLimit));
        }

        PageRequest pageRequest = PageRequest.of(requests.getFrom(), requests.getSize());

        if (requests.getSortEvents() != null) {
            Sort sort = makeOrderByClause(requests.getSortEvents());
            pageRequest = PageRequest.of(requests.getFrom(), requests.getSize(), sort);
        }

        conditions.add(event.state.eq(EventStatus.PUBLISHED));

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Page<Event> eventsPage = repository.findAll(finalCondition, pageRequest);

        if (eventsPage.isEmpty()) {
            throw new NotFoundException("events page is empty");
        }
        StatDto statDto = new StatDto("ewm-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());
        statClient.saveStat(statDto);
        for (Event eventViewed : eventsPage) {
            eventViewed.setViews(parseViews(eventViewed, request));
        }

        repository.saveAll(eventsPage);
        return toListEventShortDto(eventsPage);
    }

    @Override
    public EventDto getEventsById(int eventId, HttpServletRequest request) {
        Event event = repository.findEventsByIdAndStateIs(eventId, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("event with id  not found"));

        StatDto statDto = new StatDto("ewm-service", request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now());
        statClient.saveStat(statDto);
        event.setViews(parseViews(event, request));

        repository.save(event);
        return toEventDto(event);
    }

    private Long parseViews(Event event, HttpServletRequest request) {
        LocalDateTime st = LocalDateTime.now().minusYears(1);
        LocalDateTime end = LocalDateTime.now().plusYears(1);
        List<ViewStatsDto> viewStatsDtos = statClient.getStat(st, end, List.of("/events/" + event.getId()), true);
        if (viewStatsDtos == null || viewStatsDtos.size() == 0) {
            return 0L;
        } else {
            return viewStatsDtos.get(0).getHits();
        }
    }

    private Sort makeOrderByClause(EventsSort sortEvents) {
        switch (sortEvents) {
            case EVENT_DATE:
                return Sort.by("eventDate").descending();
            case VIEWS:
                return Sort.by("views").descending();
            default:
                return Sort.by("publishedOn").descending();
        }
    }
}