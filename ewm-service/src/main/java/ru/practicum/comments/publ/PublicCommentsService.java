package ru.practicum.comments.publ;

import ru.practicum.comments.dto.CommentDto;
import java.util.List;

public interface PublicCommentsService {
    List<CommentDto> getAllByEvent(Long eventId, int from, int size);
}
