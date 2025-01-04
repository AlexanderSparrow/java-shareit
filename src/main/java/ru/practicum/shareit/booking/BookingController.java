package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Управление бронированиями")
@RequestMapping(path = "/bookings")
public class BookingController {

    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на бронирование вещи: {} от пользователя с id: {}.", bookingDto, bookingDto.getBookerId());
        bookingDto.setStatus(BookingStatus.WAITING);
        return bookingDto;
    }

    @PatchMapping ()
}
