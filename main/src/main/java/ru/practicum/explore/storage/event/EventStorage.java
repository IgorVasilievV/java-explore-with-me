package ru.practicum.explore.storage.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventStorage extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long userId, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE ((:usersId) IS NULL OR e.initiator.id IN (:usersId)) " +
            "AND ((:eventStates) IS NULL OR e.state IN (:eventStates)) " +
            "AND ((:categoriesId) IS NULL OR e.category.id IN (:categoriesId)) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR e.eventDate > :rangeStart) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR e.eventDate < :rangeEnd) ")
    Page<Event> findEventsByAdminSearch(@Param("usersId") List<Long> usersId,
                                        @Param("eventStates") List<String> eventStates,
                                        @Param("categoriesId") List<Long> categoriesId,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        PageRequest pageRequest);

    List<Event> findAllByCategoryId(Long catId);

    List<Event> findAllByCompilationId(Long compId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE (" +
            "e.state LIKE :state) " +
            "AND (:text IS NULL OR (upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "OR (upper(e.description) like upper(concat('%', :text, '%')))) " +
            "AND ((:categoriesId) IS NULL OR e.category.id IN (:categoriesId)) " +
            "AND (:paid IS NULL OR e.paid IS :paid) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL AND e.eventDate > :dateNow OR e.eventDate > :rangeStart) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR e.eventDate < :rangeEnd) ")
    Page<Event> findEventsByPublicSearch(@Param("state") String state,
                                         @Param("text") String text,
                                         @Param("categoriesId") List<Long> categoriesId,
                                         @Param("paid") Boolean paid,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         @Param("dateNow") LocalDateTime dateNow,
                                         PageRequest pageRequest);

    List<Event> findAllByIdAndState(Long id, String string);

    List<Event> findAllByIdIn(List<Long> eventIds);
}
