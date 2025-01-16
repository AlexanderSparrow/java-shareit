package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@UtilityClass
public class BookingMapper {
    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }

    public static Booking toBooking(BookingDto dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId() > 0 ? dto.getId() : null); // Используем null для новой сущности
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItemId(dto.getItemId());
        booking.setBookerId(dto.getBookerId());
        booking.setStatus(dto.getStatus());
        return booking;
    }
}
