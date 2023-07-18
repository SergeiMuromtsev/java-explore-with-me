package ru.practicum.users.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable page = paged(from, size);
        if (ids != null && !ids.isEmpty()) {
            log.debug("admin GET: /users");
            return toListUserDto(userRepository.findAllById(ids));
        }
        log.debug("admin GET: /users");
        return toListUserDto(userRepository.findAll(page));
    }

    @Override
    public UserDto createUsers(NewUserRequest newUserRequest) {
        log.debug("admin GET: /users");
        return toUserDto(userRepository.save(mapToUser(newUserRequest)));
    }

    @Override
    public void deleteUsers(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("DELETE: user not found"));

        log.debug("admin DELETE: /users");
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("GET: user not found"));
    }
}