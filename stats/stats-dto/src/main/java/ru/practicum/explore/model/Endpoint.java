package ru.practicum.explore.model;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "endpoints", schema = "public")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private App app;

    @Column(name = "ip")
    private String ip;

    @Column(name = "created")
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Endpoint)) return false;
        Endpoint endpoint = (Endpoint) o;
        if (getId() == null) {
            return false;
        } else return getId().equals(endpoint.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
