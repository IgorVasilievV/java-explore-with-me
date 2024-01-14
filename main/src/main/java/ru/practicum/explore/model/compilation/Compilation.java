package ru.practicum.explore.model.compilation;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "compilations", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Compilation)) return false;
        Compilation compilation = (Compilation) o;
        if (getId() == null) {
            return false;
        } else if (!getId().equals(compilation.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
