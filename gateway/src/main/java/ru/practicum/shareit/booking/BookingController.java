package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

@Tag(name = "Bookings", description = "Управление бронированиями")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на создание бронирования id: {}", bookingDto);
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId,
            @RequestParam boolean approved) {
        log.info("Запрошено обновление статуса бронирования с id: {} на статус: {} пользователем: {}.", bookingId, approved, userId);
        return bookingClient.updateBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("данных о конкретном бронировании (включая его статус) id: {} пользователя с id: {}", bookingId, userId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Запрошен список всех бронирований текущего пользователя с id: {}, состояние: {}", userId, state);
        return bookingClient.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(defaultValue = "ALL") String stateParam) {
        BookingStatus state = BookingStatus.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Запрошен список бронирований для всех вещей текущего пользователя с id: {}, состояние: {}", ownerId, state);
        return bookingClient.getOwnerBookings(ownerId, state);
    }
}
