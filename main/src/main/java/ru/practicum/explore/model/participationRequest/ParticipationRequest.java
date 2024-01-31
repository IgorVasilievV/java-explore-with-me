package ru.practicum.explore.model.participationRequest;

import lombok.*;
import ru.practicum.explore.model.event.Event;
import ru.practicum.explore.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "participation_requests", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "status")
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof ParticipationRequest)) return false;
        ParticipationRequest participationRequest = (ParticipationRequest) o;
        if (getId() == null) {
            return false;
        } else if (!getId().equals(participationRequest.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ParticipationRequest{" +
                "id=" + id +
                ", event_id=" + event.getId() +
                ", status='" + status + '\'' +
                '}';
    }
}
