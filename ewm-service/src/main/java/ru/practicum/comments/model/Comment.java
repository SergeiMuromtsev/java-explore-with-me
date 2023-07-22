package ru.practicum.comments.model;

import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;
    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;
    private LocalDateTime created;
}
