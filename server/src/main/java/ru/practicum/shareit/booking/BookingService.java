package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(long userId, BookingDto bookingDto);

    BookingResponseDto updateBookingStatus(long bookingId, boolean approved, long userId);

    BookingResponseDto getBookingById(long bookingId, long userId);

    BookingDto updateBookingStatus(long bookingId, long ownerId, BookingStatus status);

    List<BookingResponseDto> getUserBookings(long userId, String state);

    List<BookingResponseDto> getOwnerBookings(long ownerId, String state);
}
