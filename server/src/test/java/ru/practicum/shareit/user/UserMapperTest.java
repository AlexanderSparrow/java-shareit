package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {

    @Test
    void toUserDto_ShouldMapUserToUserDto() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .build();

        // Act
        UserDto userDto = UserMapper.toUserDto(user);

        // Assert
        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getName(), userDto.getName());
    }

    @Test
    void toUser_ShouldMapUserDtoToUser() {
        // Arrange
        UserDto userDto = new UserDto(
                1L,
                "test@example.com",
                "Test User"
        );

        // Act
        User user = UserMapper.toUser(userDto);

        // Assert
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getName(), user.getName());
    }
}
