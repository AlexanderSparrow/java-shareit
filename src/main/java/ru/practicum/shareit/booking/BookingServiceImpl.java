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
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto createBooking(long userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь: " + bookingDto.getItemId() + " не найдена"));
        if (!item.getAvailable()) {
            throw new AccessDeniedException("Вещь недоступна для бронирования");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь: " + bookingDto.getBookerId() + " не найден"));

        boolean hasOverlap = bookingRepository.existsByItemIdAndStatusAndStartBeforeAndEndAfter(
                bookingDto.getItemId(), BookingStatus.APPROVED, bookingDto.getStart(), bookingDto.getEnd());
        if (hasOverlap) {
            throw new ValidationException("Вещь уже забронирована на указанный период");
        }


        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        booking = bookingRepository.save(booking);
        return BookingResponseMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto updateBookingStatus(long bookingId, boolean approved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id: " + bookingId + " не найдено."));

        boolean isOwner = itemRepository.existsByIdAndOwnerId(booking.getItem().getId(), userId);
        if (!isOwner) {
            throw new AccessDeniedException("Только владелец вещи может изменять статус бронирования.");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Статус бронирования уже был изменён.");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        booking = bookingRepository.save(booking);

        return BookingResponseMapper.toBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("<Бронирование с id: " + bookingId + " не найдено."));

        if (booking.getBooker().getId() != userId && !itemRepository.existsByIdAndOwnerId(booking.getItem().getId(), userId)) {
            throw new NotFoundException("Бронирование id: " + bookingId + " недоступно.");
        }

        return BookingResponseMapper.toBookingResponseDto(booking);
    }


    @Override
    public BookingDto updateBookingStatus(long bookingId, long ownerId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id " + bookingId + " не найдено"));

        Booking finalBooking = booking;
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Вещь с id " + finalBooking.getItem().getId() + " не найдена"));
        if (item.getOwnerId() != ownerId) {
            throw new ValidationException("Только владелец вещи может изменить статус бронирования");
        }

        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getUserBookings(long userId, String state) {
        BookingSearchState searchState = BookingSearchState.from(state);
        List<Booking> bookings = fetchBookingsByState(userId, searchState, false);
        return mapToResponseDtos(bookings);
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(long ownerId, String state) {
        BookingSearchState searchState = BookingSearchState.from(state);
        List<Booking> bookings = fetchBookingsByState(ownerId, searchState, true);
        if (!bookings.isEmpty()) {
            return mapToResponseDtos(bookings);
        } else {
            throw new NotFoundException("Список бронирования пользователя с id: " + ownerId + " пуст.");

        }
    }

    private List<Booking> fetchBookingsByState(long userId, BookingSearchState state, boolean isOwner) {
        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case CURRENT -> isOwner
                    ? bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now)
                    : bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now);
            case PAST -> isOwner
                    ? bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now)
                    : bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> isOwner
                    ? bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now)
                    : bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> isOwner
                    ? bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING)
                    : bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> isOwner
                    ? bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED)
                    : bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            case ALL -> isOwner
                    ? bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId)
                    : bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        };
    }

    private List<BookingResponseDto> mapToResponseDtos(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingResponseMapper::toBookingResponseDto)
                .collect(Collectors.toList());
    }

}
