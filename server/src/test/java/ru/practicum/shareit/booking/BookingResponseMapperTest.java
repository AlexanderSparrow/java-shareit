package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseMapperTest {

    @Test
    void toBookingResponseDto_createsCorrectDto() {

        Item item = new Item(1L, 1L, "Test Item", "Test Item Description", true, 1L);
        User booker = new User(1L, "Booker", "booker@example.com");
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusHours(1), item, booker, BookingStatus.APPROVED);

        // Call the method under test
        BookingResponseDto bookingResponseDto = BookingResponseMapper.toBookingResponseDto(booking);

        // Assert the results
        assertNotNull(bookingResponseDto);
        assertEquals(booking.getId(), bookingResponseDto.getId());
        assertEquals(booking.getStart(), bookingResponseDto.getStart());
        assertEquals(booking.getEnd(), bookingResponseDto.getEnd());
        assertNotNull(bookingResponseDto.getItem());
        assertEquals(booking.getItem().getId(), bookingResponseDto.getItem().getId());
        assertEquals(booking.getItem().getName(), bookingResponseDto.getItem().getName());
        assertEquals(booking.getStatus(), bookingResponseDto.getStatus());
        assertNotNull(bookingResponseDto.getBooker());
        assertEquals(booking.getBooker().getId(), bookingResponseDto.getBooker().getId());
    }

    @Test
    void toBookingResponseDto_withNullBooking_shouldThrowException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                BookingResponseMapper.toBookingResponseDto(null));
    }
}
