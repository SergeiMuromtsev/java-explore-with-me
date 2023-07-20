package ru.practicum.users.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping(path = "/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                  @RequestParam(name = "size", defaultValue = "10") int size) {

        return adminUserService.getUsers(ids, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/users")
    public UserDto createUsers(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminUserService.createUsers(newUserRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/users/{userId}")
    public void deleteUsers(@PathVariable(name = "userId") Long userId) {
        adminUserService.deleteUsers(userId);
    }
}

