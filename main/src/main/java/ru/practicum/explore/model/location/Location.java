package ru.practicum.explore.model.location;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "locations", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        if (getId() == null) {
            return false;
        } else if (!getId().equals(location.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
