package ru.practicum.explore.model.category;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "categories", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        if (getId() == null) {
            return false;
        } else if (!getId().equals(category.getId()))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
