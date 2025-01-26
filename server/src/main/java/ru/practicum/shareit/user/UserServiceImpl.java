package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto) // Преобразование из User в UserDto
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserMapper::toUserDto) // Преобразование из User в UserDto
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto); // Преобразование из UserDto в User
        return UserMapper.toUserDto(userRepository.save(user)); // Сохранение и преобразование обратно
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        return UserMapper.toUserDto(userRepository.save(user)); // Сохранение обновленной сущности
    }

    @Override
    public void deleteById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        userRepository.deleteById(userId);
    }
}
