package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(1L, "Test User", "test@example.com");
        item = new Item(1L, 1L, "Item Name", "Item Description", true, user.getId());
        booking = new Booking(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item,
                user,
                BookingStatus.WAITING
        );
    }

    @Test
    void createBooking_ValidInput_ShouldReturnBookingResponseDto() {
        BookingDto bookingDto = new BookingDto(0, booking.getStart(), booking.getEnd(), item.getId(), user.getId(), BookingStatus.WAITING);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.existsByItemIdAndStatusAndStartBeforeAndEndAfter(
                eq(item.getId()), eq(BookingStatus.APPROVED), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto response = bookingService.createBooking(user.getId(), bookingDto);

        assertNotNull(response);
        assertEquals(booking.getId(), response.getId());
        assertEquals(BookingStatus.WAITING, response.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_ItemNotFound_ShouldThrowNotFoundException() {
        BookingDto bookingDto = new BookingDto(0, booking.getStart(), booking.getEnd(), 999L, user.getId(), BookingStatus.WAITING);

        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(user.getId(), bookingDto));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_ItemUnavailable_ShouldThrowAccessDeniedException() {
        item.setAvailable(false);
        BookingDto bookingDto = new BookingDto(0, booking.getStart(), booking.getEnd(), item.getId(), user.getId(), BookingStatus.WAITING);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(AccessDeniedException.class, () -> bookingService.createBooking(user.getId(), bookingDto));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_ValidInput_ShouldUpdateStatus() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.existsByIdAndOwnerId(booking.getItem().getId(), user.getId())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDto response = bookingService.updateBookingStatus(booking.getId(), true, user.getId());

        assertNotNull(response);
        assertEquals(BookingStatus.APPROVED, response.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_NotOwner_ShouldThrowAccessDeniedException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.existsByIdAndOwnerId(booking.getItem().getId(), user.getId())).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> bookingService.updateBookingStatus(booking.getId(), true, user.getId()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBookingById_ValidInput_ShouldReturnBookingResponseDto() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.existsByIdAndOwnerId(item.getId(), user.getId())).thenReturn(true);

        BookingResponseDto response = bookingService.getBookingById(booking.getId(), user.getId());

        assertNotNull(response);
        assertEquals(booking.getId(), response.getId());
    }

    @Test
    void getBookingById_NotOwnerOrBooker_ShouldThrowNotFoundException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.existsByIdAndOwnerId(item.getId(), user.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(booking.getId(), 999L));
    }

    @Test
    void getUserBookings_ValidInput_ShouldReturnList() {
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(user.getId())).thenReturn(List.of(booking));

        List<BookingResponseDto> response = bookingService.getUserBookings(user.getId(), "ALL");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(booking.getId(), response.get(0).getId());
    }
}

