package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Map<Long, User> getAllUsers() {
        log.info("Получен запрс на вывод всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        log.info("Получен запрос на вывод пользователя с id: {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получен запрос на удаление пользователя с id: {}", userId);
        userService.deleteById(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на полное обновление пользователя: {}", user);
        return userService.updateUser(user);
    }

    @PatchMapping("/{id}")
    public User partiallyUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        log.info("Получен запрос на частичное обновление пользователя с id: {}", id);
        return userService.partialUpdate(id, updates); // Частичное обновление
    }

}

