package ru.practicum.events.priv.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.publ.service.PublicCategoriesService;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UserUpdateEventRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ValidateException;
import ru.practicum.locations.service.LocationService;
import ru.practicum.users.admin.service.AdminUserServiceImpl;
import ru.practicum.users.model.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.categories.mapper.CategoryMapper.toCategory;
import static ru.practicum.events.mapper.EventsMapper.*;
import static ru.practicum.utils.Page.paged;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class PrivateEventsServiceImpl implements PrivateEventsService {
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private AdminUserServiceImpl adminUserService;
    @Autowired
    private PublicCategoriesService publicCategoriesService;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEventsByUser(int userId, int from, int size) {
        Pageable page = paged(from, size);
        return toListEventShortDto(eventsRepository.findEventsByInitiator_Id(userId, page));
    }

    @Override
    public EventDto createEvents(Integer userId, NewEventDto newEventDto) {
        validTime(newEventDto.getEventDate());

        User user = adminUserService.getUserById(userId);
        Category category = publicCategoriesService.getCatById(newEventDto.getCategory());
        locationService.save(newEventDto.getLocation());
        Event event = toEvent(newEventDto, category, user);
        return toEventDto(eventsRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public EventDto getEventsByUserFullInfo(int userId, int eventId) {
        return toEventDto(eventsRepository
                .findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("")));
    }

    @Override
    public EventDto changeEventsByUser(int userId, int eventId, UserUpdateEventRequest userUpdateEventRequest) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("ExceptionMessages.NOT_FOUND_EVENTS_EXCEPTION.label)"));

        if (event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED)) {
            if (userUpdateEventRequest.getEventDate() != null) {
                validTime(userUpdateEventRequest.getEventDate());
                event.setEventDate(LocalDateTime.parse(userUpdateEventRequest.getEventDate()));
            }
            if (userUpdateEventRequest.getAnnotation() != null) {
                event.setAnnotation(userUpdateEventRequest.getAnnotation());
            }
            if (userUpdateEventRequest.getCategory() != null) {
                event.setCategory(toCategory(userUpdateEventRequest.getCategory()));
            }
            if (userUpdateEventRequest.getDescription() != null) {
                event.setDescription(userUpdateEventRequest.getDescription());
            }
            if (userUpdateEventRequest.getLocation() != null) {
                event.setLocation(userUpdateEventRequest.getLocation());
            }
            if (userUpdateEventRequest.getStateAction() != null) {
                switch (userUpdateEventRequest.getStateAction()) {
                    case SEND_TO_REVIEW:
                        event.setState(EventStatus.PENDING);
                        break;
                    case CANCEL_REVIEW:
                        event.setState(EventStatus.CANCELED);
                        break;
                }
            }
            if (userUpdateEventRequest.getTitle() != null) {
                event.setTitle(userUpdateEventRequest.getTitle());
            }

            return toEventDto(eventsRepository.save(event));
        } else {
            throw new ConflictException("");
        }
    }

    @Override
    public Event getEventById(int eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found"));
    }

    private void validTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(time, formatter);

        if (Duration.between(LocalDateTime.now(), startDate).toMinutes() < Duration.ofHours(2).toMinutes()) {
            throw new ValidateException("");
        }
    }
}