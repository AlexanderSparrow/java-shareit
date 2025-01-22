package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user@example.com", "John Doe");
        userDto = new UserDto(1L, "user@example.com", "John Doe");
    }

    @Test
    void getAll_ShouldReturnListOfUserDtos() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.getFirst().getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_UserExists_ShouldReturnUserDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getUserById_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.getUserById(user.getId()));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void createUser_ShouldSaveAndReturnUserDto() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_UserExists_ShouldUpdateAndReturnUserDto() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedDto = new UserDto(null, "newemail@example.com", "New Name");
        UserDto result = userService.updateUser(user.getId(), updatedDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals("newemail@example.com", result.getEmail());
        assertEquals("New Name", result.getName());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUser(user.getId(), userDto));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void deleteById_UserExists_ShouldDeleteUser() {
        when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.deleteById(user.getId());

        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteById_UserNotFound_ShouldThrowException() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.deleteById(user.getId()));

        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, times(1)).existsById(user.getId());
    }
}
