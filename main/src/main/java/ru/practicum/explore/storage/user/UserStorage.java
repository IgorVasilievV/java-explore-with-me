package ru.practicum.explore.storage.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserStorage extends JpaRepository<User, Long> {
    @Query("select u.id from User u")
    List<Long> findAllIds();

    Page<User> findAllByIdIn(Long[] ids, PageRequest pageRequest);
}
