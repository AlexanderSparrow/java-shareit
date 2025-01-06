package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemResponseMapper;
import ru.practicum.shareit.user.dto.BookerDto;

@UtilityClass
public class BookingResponseMapper {
    public BookingResponseDto toBookingResponseDto(Booking booking, String itemName) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemResponseMapper.toItemResponseDto(booking.getItemId(), itemName),
                new BookerDto(booking.getBookerId()),
                booking.getStatus()
        );
    }
}
