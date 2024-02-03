package ru.practicum.explore.model.event;

import lombok.*;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.comment.Comment;
import ru.practicum.explore.model.compilation.Compilation;
import ru.practicum.explore.model.location.Location;
import ru.practicum.explore.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "events", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "is_paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "is_request_moderation")
    private Boolean requestModeration;

    @Column(name = "state")
    private String state;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("publishedOn DESC")
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        if (getId() == null) {
            return false;
        } else if (!getId().equals(event.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
