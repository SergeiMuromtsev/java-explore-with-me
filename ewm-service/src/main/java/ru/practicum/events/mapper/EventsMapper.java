package ru.practicum.events.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStatus;
import ru.practicum.users.model.User;

import static ru.practicum.categories.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.users.mapper.UserMapper.toUserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.isRequestModeration())
                .state(EventStatus.PENDING)
                .title(newEventDto.getTitle())
                .views(0L)
                .build();
    }

    public static EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().toString().replace("T", " "))
                .description(event.getDescription())
                .eventDate(event.getEventDate().toString().replace("T", " "))
                .initiator(toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().toString().replace("T", " "))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }


    public static List<EventShortDto> toListEventShortDto(Page<Event> listEvent) {
        List<EventShortDto> listEventShortDto = new ArrayList<>();
        for (Event event: listEvent) {
            listEventShortDto.add(toEventShortDto(event));
        }
        return listEventShortDto;
    }

    public static List<EventShortDto> toListEventShortDto(List<Event> listEvent) {
        List<EventShortDto> listEventShortDto = new ArrayList<>();
        for (Event event: listEvent) {
            listEventShortDto.add(toEventShortDto(event));
        }
        return listEventShortDto;
    }

    public static List<EventDto> toListEventDto(List<Event> listEvent) {
        List<EventDto> listEventFullDto = new ArrayList<>();
        for (Event event: listEvent) {
            listEventFullDto.add(toEventDto(event));
        }

        return listEventFullDto;
    }

    public static List<EventDto> toListEventDto(Page<Event> listEvent) {
        List<EventDto> listEventFullDto = new ArrayList<>();
        for (Event event: listEvent) {
            listEventFullDto.add(toEventDto(event));
        }

        return listEventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().toString().replace("T", " "))
                .initiator(toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
