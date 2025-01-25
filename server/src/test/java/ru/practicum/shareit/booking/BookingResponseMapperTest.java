package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingResponseMapperTest {

    private Item item;
    private User booker;
    private Booking booking;

    @BeforeEach
    void setUp() {
        item = new Item(1L, 1L, "Item", "Item Description", true, 1L);
        booker = new User(1L, "Booker", "booker@example.com");
        booking = new Booking(1L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), item, booker, BookingStatus.APPROVED);
    }

    @Test
    void toBookingResponseDto_ShouldReturnCorrectBookingResponseDto() {
        BookingResponseDto bookingResponseDto = BookingResponseMapper.toBookingResponseDto(booking);

        assertNotNull(bookingResponseDto);
        assertEquals(booking.getId(), bookingResponseDto.getId());
        assertEquals(booking.getStart(), bookingResponseDto.getStart());
        assertEquals(booking.getEnd(), bookingResponseDto.getEnd());
        assertEquals(item.getId(), bookingResponseDto.getItem().getId());
        assertEquals(item.getName(), bookingResponseDto.getItem().getName());
        assertEquals(booker.getId(), bookingResponseDto.getBooker().getId());
        assertEquals(booking.getStatus(), bookingResponseDto.getStatus());
    }

    @Test
    void toBookingResponseDto_WithBookingWithoutStatus_ShouldReturnBookingResponseDtoWithNullStatus() {
        booking.setStatus(null);
        BookingResponseDto bookingResponseDto = BookingResponseMapper.toBookingResponseDto(booking);

        assertNotNull(bookingResponseDto);
        assertNull(bookingResponseDto.getStatus());
    }
}
