package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateKeyException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .values()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId));
    }

    @Override
    public void deleteById(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUser(userDto);

        if (emailExists(userDto.getEmail())) {
            throw new DuplicateKeyException("Пользователь с таким email уже существует!");
        }

        User user = UserMapper.toUser(userDto);
        User createdUser = userRepository.create(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId());

        if (userDto.getEmail() != null) {
            if (emailExists(userDto.getEmail(), userDto.getId())) {
                throw new DuplicateKeyException("Пользователь с таким email уже существует!");
            }
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        User updatedUser = userRepository.update(existingUser);
        return UserMapper.toUserDto(updatedUser);
    }

    private void validateUser(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            throw new ValidationException("Имя пользователя должно быть указано!");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ValidationException("E-mail пользователя должен быть указан!");
        }
    }

    private boolean emailExists(String email) {
        return userRepository.findAll()
                .values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean emailExists(String email, Long id) {
        return userRepository.findAll()
                .values()
                .stream()
                .anyMatch(user -> user.getId() != id && user.getEmail().equals(email));
    }
}
