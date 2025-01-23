package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingMapperTest {

    @Test
    void toBookingDto_shouldMapCorrectly() {
        // Arrange
        User booker = new User(1L, "User1", "user1@example.com");
        Item item = new Item(1L, 2L, "Item1", "Item description", true, null);
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, booker, BookingStatus.APPROVED);

        // Act
        BookingDto result = BookingMapper.toBookingDto(booking);

        // Assert
        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
        assertEquals(booking.getItem().getId(), result.getItemId());
        assertEquals(booking.getBooker().getId(), result.getBookerId());
        assertEquals(booking.getStatus(), result.getStatus());
    }
}