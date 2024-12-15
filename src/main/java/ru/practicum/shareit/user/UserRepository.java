package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    List<User> findAll();

    User findById(long userId);

    void delete(long userId);

    User create(User user);

    User update(User user);

    User partialUpdate(Long id, Map<String, Object> updates);

    boolean existsById(long userId);
}
