package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateKeyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public Map<Long, User> findAll() {
        return users;
    }

    @Override
    public User findById(long userId) {
        return users.get(userId);
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
        log.info("Пользователь с id: {} удален.", userId);
    }

    @Override
    public User create(User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new DuplicateKeyException("Пользователь с таким email уже существует!");
        }
        if (user.getName() == null || (user.getName().isEmpty())) {
            throw new ValidationException("Имя пользователя должно быть указано!");
        }
        if (user.getEmail() == null || (user.getEmail().isEmpty())) {
            throw new ValidationException("E-mail пользователя должен быть указан!");
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Создан пользователь: {}.", user);
        return user;
    }

    @Override
    public User update(User user) {
        long id = user.getId();
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден!");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("E-mail пользователя должен быть указан!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new ValidationException("Имя пользователя должно быть указано!");
        }
        users.put(id, user);
        log.info("Пользователь: {} обновлен.", user);
        return user;
    }

    @Override
    public User partialUpdate(Long id, Map<String, Object> updates) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id: " + id + " не найден!");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (value == null || ((String) value).isEmpty()) {
                        throw new ValidationException("Имя пользователя должно быть указано!");
                    }
                    user.setName((String) value);
                    break;

                case "email":
                    if (value == null || ((String) value).isEmpty()) {
                        throw new ValidationException("E-mail пользователя должен быть указан!");
                    }
                    String email = (String) value;
                    if (users.values().stream().anyMatch(u -> u.getId() != id && u.getEmail().equals(email))) {
                        throw new DuplicateKeyException("Пользователь с таким email уже существует!");
                    }
                    user.setEmail(email);
                    break;

                default:
                    throw new ValidationException("Поле " + key + " не поддерживается для обновления!");
            }
        });

        users.put(id, user); // Обновляем пользователя в хранилище
        log.info("Пользователь с id: {} обновлен частично: {}", id, updates);
        return user;
    }

}
