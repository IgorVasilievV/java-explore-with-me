package ru.practicum.explore.service.user;

import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.dto.UserDtoIn;
import ru.practicum.explore.model.user.dto.UserDtoOut;
import ru.practicum.explore.model.user.dto.UserDtoShortOut;

public class UserMapper {
    public static User toUser(UserDtoIn userDtoIn) {
        return User.builder()
                .email(userDtoIn.getEmail())
                .name(userDtoIn.getName())
                .build();
    }

    public static UserDtoOut toUserDtoOut(User user) {
        return UserDtoOut.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserDtoShortOut toUserDtoShortOut(User user) {
        return UserDtoShortOut.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
