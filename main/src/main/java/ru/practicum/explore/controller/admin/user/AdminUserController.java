package ru.practicum.explore.controller.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.user.dto.UserDtoOut;
import ru.practicum.explore.model.user.dto.UserDtoIn;
import ru.practicum.explore.model.user.dto.UserDtoOut;
import ru.practicum.explore.service.user.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDtoOut addUser(@Valid @RequestBody UserDtoIn userDtoIn) {
        return userService.addUser(userDtoIn);
    }

    @GetMapping
    public List<UserDtoOut> getUsers(@RequestParam(required = false) Long[] ids,
                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void patchCategory(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
