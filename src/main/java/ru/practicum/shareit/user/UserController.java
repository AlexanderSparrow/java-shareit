package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрс на вывод всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("Получен запрос на вывод пользователя с id: {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя с id: {}", userId);
        userService.deleteById(userId);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос на добавление пользователя: {}", userDto);
        return userService.createUser(userDto);
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос на полное обновление пользователя: {}", userDto);
        return userService.updateUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto partiallyUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        log.info("Получен запрос на частичное обновление пользователя с id: {}", id);
        return userService.partialUpdate(id, updates); // Частичное обновление
    }

}

