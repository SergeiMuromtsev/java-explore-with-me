package ru.practicum.users.admin.service;

import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getUsers(List<Integer> ids, int from, int size);

    UserDto createUsers(NewUserRequest newUserRequest);

    void deleteUsers(int userId);

    User getUserById(int userId);
}
