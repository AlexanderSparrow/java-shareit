package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateKeyException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long nextId = 1;

    @Override
    public List<User> findAll() {
        log.info("Запрошен список всех пользователей. Количество пользователей: {}", users.size());
        return new ArrayList<>(users);
    }

    @Override
    public User findById(long userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Пользователь с id: {} не найден.", userId);
                    return new NotFoundException("Пользователь с id: " + userId + " не найден!");
                });
    }

    @Override
    public void delete(long userId) {
        User user = findById(userId);
        users.remove(user);
        log.info("Пользователь с id: {} удален.", userId);
    }

    @Override
    public User create(User user) {
        validateUser(user);
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new DuplicateKeyException("Пользователь с таким email уже существует!");
        }
        user.setId(nextId++);
        users.add(user);
        log.info("Создан пользователь: {}.", user);
        return user;
    }

    @Override
    public User update(User user) {
        validateUser(user);
        User existingUser = findById(user.getId());
        if (users.stream().anyMatch(u -> u.getId() != user.getId() && u.getEmail().equals(user.getEmail()))) {
            throw new DuplicateKeyException("Пользователь с таким email уже существует!");
        }
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        log.info("Пользователь: {} обновлен.", existingUser);
        return existingUser;
    }

    @Override
    public User partialUpdate(Long id, Map<String, Object> updates) {
        User user = findById(id);

        updates.forEach((key, value) -> {
            try {
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);
                if (value == null || value.toString().isEmpty()) {
                    throw new ValidationException("Поле " + key + " должно быть указано!");
                }
                if (key.equals("email") && users.stream()
                        .anyMatch(u -> u.getId() != id && u.getEmail().equals(value.toString()))) {
                    throw new DuplicateKeyException("Пользователь с таким email уже существует!");
                }
                field.set(user, value);
                log.info("Обновлено поле {} у пользователя с id {}: {}", key, id, value);
            } catch (NoSuchFieldException e) {
                throw new ValidationException("Поле " + key + " не поддерживается для обновления!");
            } catch (IllegalAccessException e) {
                throw new ValidationException("Ошибка при обновлении поля " + key);
            }
        });

        return user;
    }

    @Override
    public boolean existsById(long userId) {
        return users.stream().anyMatch(user -> user.getId() == userId);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new ValidationException("Имя пользователя должно быть указано!");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("E-mail пользователя должен быть указан!");
        }
    }
}