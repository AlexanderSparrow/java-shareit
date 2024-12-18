package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public Map<Long, User> findAll() {
        log.info("Запрошен список всех пользователей. Количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public User findById(long userId) {
        User user = users.get(userId);
        if (user == null) {
            log.warn("Пользователь с id: {} не найден!", userId);
            throw new NotFoundException("Пользователь с id: " + userId + "не найден!");
        }
        return user;
    }

    @Override
    public void delete(long userId) {
        if (users.remove(userId) == null) {
            log.warn("Попытка удалить несуществующего пользователя с id: {}", userId);
            throw new NotFoundException("Пользователь с id: " + userId + "не найден!");
        }
        log.info("Пользователь с id: {} удален.", userId);
    }

    @Override
    public User create(User user) {
        user.setId(nextId);
        users.put(nextId++, user);
        log.info("Создан пользователь: {}.", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Попытка обновить несуществующего пользователя с id: {}", user.getId());
            throw new NotFoundException("Пользователь с id: " + user.getId() + " не найден!");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean existsById(long userId) {
        return users.containsKey(userId);
    }
}