package ru.practicum.explore.storage.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.model.compilation.Compilation;

public interface CompilationStorage extends JpaRepository<Compilation, Long> {

    @Query("SELECT c " +
            "FROM Compilation AS c " +
            "WHERE (:isPinned IS NULL OR c.pinned IS :isPinned) ")
    Page<Compilation> findAllCompilations(@Param("isPinned") Boolean pinned,
                                          PageRequest pageRequest);
}
