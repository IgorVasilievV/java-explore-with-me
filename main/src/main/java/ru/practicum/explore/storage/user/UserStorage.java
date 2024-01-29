package ru.practicum.explore.storage.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserStorage extends JpaRepository<User, Long> {

    @Query("select u from User u " +
            "WHERE ((:ids) IS NULL OR u.id IN (:ids)) ")
    Page<User> findAllByIdIn(@Param("ids") List<Long> ids,
                             PageRequest pageRequest);
}
