package ru.practicum.shareit.user;

import java.util.Map;

public interface UserService {
    Map<Long, User> getAll();

    User getUserById(long userId);

    void deleteById(long userId);

    User createUser(User user);

    User updateUser(User user);

    User partialUpdate(Long id, Map<String, Object> updates);
}
