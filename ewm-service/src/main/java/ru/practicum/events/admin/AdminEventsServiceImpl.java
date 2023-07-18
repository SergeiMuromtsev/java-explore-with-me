package ru.practicum.events.admin;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.admin.AdminCategoriesService;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.AdminEventRequests;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.AdminUpdateEventRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.model.QEvent;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidateException;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.service.LocationService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.events.mapper.EventsMapper.toEventDto;
import static ru.practicum.events.mapper.EventsMapper.toListEventDto;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminEventsServiceImpl implements AdminEventsService {
    private final EventsRepository repository;
    private final AdminCategoriesService categoriesService;
    private final LocationService locationService;

    @Transactional(readOnly = true)
    @Override
    public List<EventDto> findEvents(AdminEventRequests requests) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (requests.hasUsers()) {
            for (Long id : requests.getUsers()) {
                conditions.add(event.initiator.id.eq(id));
            }
        }

        if (requests.hasStates()) {
            for (Long id : requests.getCategories()) {
                conditions.add(event.category.id.eq(id));
            }
        }

        if (requests.hasCategories()) {
            for (Long id : requests.getCategories()) {
                conditions.add(event.category.id.eq(id));
            }
        }

        if (requests.getRangeStart() != null && requests.getRangeEnd() != null) {
            conditions.add(event.eventDate.between(requests.getRangeStart(), requests.getRangeEnd()));
        }

        PageRequest pageRequest = PageRequest.of(requests.getFrom(), requests.getSize());
        Page<Event> eventsPage;

        if (!conditions.isEmpty()) {
            BooleanExpression finalCondition = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();

            eventsPage = repository.findAll(finalCondition, pageRequest);
        } else {
            eventsPage = repository.findAll(pageRequest);
        }

        log.debug("admin GET events service");
        return toListEventDto(eventsPage);
    }

    @Override
    public EventDto changeEvents(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = repository.findById(eventId).orElseThrow(() -> new NotFoundException(""));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            Category category = categoriesService.findCategoriesById(adminUpdateEventRequest.getCategory());
            event.setCategory(category);
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            LocalDateTime startOldDate = event.getCreatedOn();
            LocalDateTime startNewDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter);

            if (Duration.between(startOldDate, startNewDate).toMinutes() < Duration.ofHours(1).toMinutes()) {
                throw new ValidateException("wrong duration - admin events service");
            }

            event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            Location location = locationService.save(adminUpdateEventRequest.getLocation());
            event.setLocation(location);
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }

        if (event.getState().equals(EventStatus.PENDING)) {
            if (adminUpdateEventRequest.getStateAction() != null) {
                switch (adminUpdateEventRequest.getStateAction()) {
                    case PUBLISH_EVENT:
                        event.setState(EventStatus.PUBLISHED);
                        break;
                    case REJECT_EVENT:
                        event.setState(EventStatus.CANCELED);
                        break;
                }
            }
        } else {
            throw new ConflictException("conflict admin events service change");
        }

        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }

        log.debug("");
        return toEventDto(repository.save(event));
    }
}