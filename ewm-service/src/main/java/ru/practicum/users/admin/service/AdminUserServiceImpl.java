package ru.practicum.users.admin.service;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;

import static ru.practicum.users.mapper.UserMapper.*;
import static ru.practicum.utils.Page.paged;

@Service
@Transactional
@NoArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        Pageable page = paged(from, size);
        if (ids != null && !ids.isEmpty()) {
            log.debug("admin GET users");
            return toListUserDto(repository.findAllById(ids));
        }
        log.debug("admin GET users");
        return toListUserDto(repository.findAll(page));
    }

    @Override
    public UserDto createUsers(NewUserRequest newUserRequest) {
        log.debug("admin GET users");
        return toUserDto(repository.save(mapToUser(newUserRequest)));
    }

    @Override
    public void deleteUsers(int userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("DELETE: user not found"));

        log.debug("admin DELETE users");
        repository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(int userId) {
        return repository.findById(userId).orElseThrow(() -> new NotFoundException("GET: user not found"));
    }
}