package ru.practicum.explore.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.dto.UserDtoIn;
import ru.practicum.explore.model.user.dto.UserDtoOut;
import ru.practicum.explore.service.user.UserMapper;
import ru.practicum.explore.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final ValidationUserService validationUserService;

    public UserDtoOut addUser(UserDtoIn userDtoIn) {
        User userFromDb = userStorage.save(UserMapper.toUser(userDtoIn));
        log.info("add new user with id=" + userFromDb.getId());
        return UserMapper.toUserDtoOut(userFromDb);
    }

    public List<UserDtoOut> getUsers(Long[] ids, Integer from, Integer size) {
        validationUserService.validatePagination(from, size);
        if (ids == null) {
            List<Long> idsFromDb = userStorage.findAllIds();
            ids = new Long[idsFromDb.size()];
            ids = idsFromDb.toArray(ids);
        }
        Sort sort = Sort.by(("id")).ascending();
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        log.info("get users");
        Page<User> usersPage = userStorage.findAllByIdIn(ids, pageRequest);
        return usersPage.stream()
                .map(UserMapper::toUserDtoOut)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        validationUserService.validateId(userId);
        userStorage.deleteById(userId);
        log.info("delete user with id=" + userId);
    }
}
