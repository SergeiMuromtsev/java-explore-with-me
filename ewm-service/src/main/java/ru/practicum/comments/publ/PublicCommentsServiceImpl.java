package ru.practicum.comments.publ;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.events.model.EventStatus;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.Page;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PublicCommentsServiceImpl implements PublicCommentsService {
    private final CommentsRepository commentRepository;
    private final EventsRepository eventsRepository;

    @Override
    public List<CommentDto> getAllCommentsByEvent(Long eventId, int from, int size) {
        Pageable page = Page.paged(from, size);
        eventsRepository.findEventsByIdAndStateIs(eventId, EventStatus.PUBLISHED).orElseThrow(
                () -> new NotFoundException(""));
        return commentRepository.findAll(page)
                .stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
