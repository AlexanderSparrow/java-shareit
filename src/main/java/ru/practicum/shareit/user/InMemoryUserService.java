package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(long userId) {
        return userMapper.toUserDto(userRepository.findById(userId));
    }

    @Override
    public void deleteById(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User createdUser = userRepository.create(user);
        return userMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User updatedUser = userRepository.create(user);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto partialUpdate(Long id, Map<String, Object> updates) {
        return userMapper.toUserDto(userRepository.partialUpdate(id, updates));
    }
}
