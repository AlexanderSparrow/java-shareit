package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseMapperTest {

    @Test
    void toBookingResponseDto_createsCorrectDto() {
        // Arrange
        Item item = new Item(1L, 1L, "Test Item", "Test Item Description", true, 1L);
        User booker = new User(1L, "Booker", "booker@example.com");
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), item, booker, BookingStatus.APPROVED);

        // Mock ItemResponseMapper and BookerDto
        ItemShortResponseDto itemShortResponseDto = new ItemShortResponseDto(item.getId(), item.getName());
        BookerDto bookerDto = new BookerDto(booker.getId());

        // Act
        BookingResponseDto bookingResponseDto = BookingResponseMapper.toBookingResponseDto(booking);

        // Assert
        assertNotNull(bookingResponseDto);
        assertEquals(booking.getId(), bookingResponseDto.getId());
        assertEquals(booking.getStart(), bookingResponseDto.getStart());
        assertEquals(booking.getEnd(), bookingResponseDto.getEnd());
        assertEquals(itemShortResponseDto, bookingResponseDto.getItem());
        assertEquals(bookerDto, bookingResponseDto.getBooker());
        assertEquals(booking.getStatus(), bookingResponseDto.getStatus());
    }


    @Test
    void toBookingResponseDto_handlesNullBookingGracefully() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                BookingResponseMapper.toBookingResponseDto(null));
    }

    @Test
    void toBookingResponseDto_createsCorrectDtoWithDifferentStatus() {
        // Arrange
        Item item = new Item(1L, 1L, "Test Item", "Test Item Description", true, 1L);
        User booker = new User(1L, "Booker", "booker@example.com");
        Booking booking = new Booking(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), item, booker, BookingStatus.REJECTED);

        // Act
        BookingResponseDto bookingResponseDto = BookingResponseMapper.toBookingResponseDto(booking);

        // Assert
        assertNotNull(bookingResponseDto);
        assertEquals(BookingStatus.REJECTED, bookingResponseDto.getStatus());
    }
}
