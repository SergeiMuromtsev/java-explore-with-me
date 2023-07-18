package ru.practicum.events.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.publ.PublicCategoriesService;
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
import ru.practicum.users.admin.AdminUserServiceImpl;
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
@RequiredArgsConstructor
@Slf4j
public class PrivateEventsServiceImpl implements PrivateEventsService {

    private final EventsRepository eventsRepository;
    private final AdminUserServiceImpl adminUserService;
    private final LocationService locationService;
    private final PublicCategoriesService publicCategoriesService;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEventsByUser(Long userId, int from, int size) {
        Pageable page = paged(from, size);
        return toListEventShortDto(eventsRepository.findEventsByInitiator_Id(userId, page));
    }

    @Override
    public EventDto createEvents(Long userId, NewEventDto newEventDto) {
        timeValidation(newEventDto.getEventDate());

        User user = adminUserService.getUserById(userId);
        Category category = publicCategoriesService.getCategoryById(newEventDto.getCategory());
        locationService.save(newEventDto.getLocation());
        Event event = toEvent(newEventDto, category, user);
        return toEventDto(eventsRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public EventDto getEventsByUserFullInfo(Long userId, Long eventId) {
        return toEventDto(eventsRepository
                .findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("event with id " + eventId + " not found")));
    }

    @Override
    public EventDto changeEventsByUser(Long userId, Long eventId, UserUpdateEventRequest userUpdateEventRequest) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event with id " + eventId + " not found"));

        if (event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED)) {
            if (userUpdateEventRequest.getEventDate() != null) {
                timeValidation(userUpdateEventRequest.getEventDate());
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
    public Event getEventById(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found"));
    }

    private void timeValidation(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(time, formatter);
        if (Duration.between(LocalDateTime.now(), startDate).toMinutes() < Duration.ofHours(2).toMinutes()) {
            throw new ValidateException("wrong time PrivateEventsService");
        }
    }
}