package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> getAll();

    UserDto getUserById(long userId);

    void deleteById(long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto partialUpdate(Long id, Map<String, Object> updates);
}
