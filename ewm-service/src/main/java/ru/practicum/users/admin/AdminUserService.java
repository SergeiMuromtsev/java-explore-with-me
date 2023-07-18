package ru.practicum.users.admin;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUsers(NewUserRequest newUserRequest);

    void deleteUsers(Long userId);

    User getUserById(Long userId);
}
