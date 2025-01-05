package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto createBooking(BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + bookingDto.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new AccessDeniedException("Вещь недоступна для бронирования");
        }

        boolean hasOverlap = bookingRepository.existsByItemIdAndDateRange(
                bookingDto.getItemId(), bookingDto.getStart(), bookingDto.getEnd());
        if (hasOverlap) {
            throw new ValidationException("Вещь уже забронирована на указанный период");
        }

        if (!userRepository.existsById(bookingDto.getBookerId())) {
            throw new NotFoundException("Пользователь с id " + bookingDto.getBookerId() + " не найден");
        }

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        return BookingResponseMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto updateBookingStatus(long bookingId, boolean approved, long userId) {
        // Найти бронирование
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдено."));

        // Проверить, что текущий пользователь является владельцем вещи
        boolean isOwner = itemRepository.isOwner(userId, booking.getItemId());
        if (!isOwner) {
            throw new AccessDeniedException("Только владелец вещи может изменять статус бронирования.");
        }

        // Обновить статус бронирования
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Статус бронирования уже был изменён.");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);

        // Вернуть результат
        return BookingResponseMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("<Бронирование с id: " + bookingId + " не найдено."));

        if (booking.getBookerId() != userId && !itemRepository.isOwner(userId, booking.getItemId())) {
            throw new NotFoundException("Бронирование id: " + bookingId + " недоступно.");
        }

        return BookingResponseMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingDto updateBookingStatus(long bookingId, long ownerId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        Booking finalBooking = booking;
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + finalBooking.getItemId() + " не найдена"));
        if (item.getOwnerId() != ownerId) {
            throw new ValidationException("Только владелец вещи может изменить статус бронирования");
        }

        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getUserBookings(long userId, String state) {
        List<Booking> bookings = fetchBookingsByState(userId, state, false);
        return bookings.stream()
                .map(BookingResponseMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(long ownerId, String state) {
        List<Booking> bookings = fetchBookingsByState(ownerId, state, true);
        if (!bookings.isEmpty()) {
            return bookings.stream()
                    .map(BookingResponseMapper::toBookingResponseDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Список бронирования пользователя с id: " + ownerId + " пуст.");
        }
    }

    private List<Booking> fetchBookingsByState(long userId, String state, boolean isOwner) {
        Instant now = Instant.now();
        return switch (state.toUpperCase()) {
            case "CURRENT" -> isOwner ? bookingRepository.findCurrentByOwnerId(userId, now)
                    : bookingRepository.findCurrentByBookerId(userId, now);
            case "PAST" -> isOwner ? bookingRepository.findPastByOwnerId(userId, now)
                    : bookingRepository.findPastByBookerId(userId, now);
            case "FUTURE" -> isOwner ? bookingRepository.findFutureByOwnerId(userId, now)
                    : bookingRepository.findFutureByBookerId(userId, now);
            case "WAITING" -> isOwner ? bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.WAITING)
                    : bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case "REJECTED" -> isOwner ? bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED)
                    : bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED);
            default -> isOwner ? bookingRepository.findAllByOwnerId(userId)
                    : bookingRepository.findAllByBookerId(userId);
        };
    }

}
