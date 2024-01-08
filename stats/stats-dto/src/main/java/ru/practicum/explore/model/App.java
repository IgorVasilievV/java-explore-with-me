package ru.practicum.explore.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "apps", schema = "public")
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "uri")
    private String uri;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof App)) return false;
        App app = (App) o;
        if (getId() == null) {
            return false;
        } else return getId().equals(app.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
