package ru.practicum.shareit.user;

import java.util.Map;

public interface UserRepository {
    Map<Long, User> findAll();

    User findById(long userId);

    void delete(long userId);

    User create(User user);

    User update(User user);

    User partialUpdate(Long id, Map<String, Object> updates);
}
