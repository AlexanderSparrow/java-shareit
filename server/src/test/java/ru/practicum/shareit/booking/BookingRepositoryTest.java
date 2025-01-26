package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase
@Transactional
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManager entityManager;

    private Item item;
    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        // Создание тестового объекта Owner
        User owner = User.builder()
                .email("owner@ya.ru")
                .name("Item Owner")
                .build();
        entityManager.persist(owner);

        // Создание тестового объекта Item
        item = Item.builder()
                .name("Test Item")
                .description("Test Description")
                .ownerId(owner.getId())
                .available(true)
                .build();
        entityManager.persist(item);

        // Создание тестового объекта Booker
        User booker = User.builder()
                .email("booker@ya.ru")
                .name("Jon Doe")
                .build();
        entityManager.persist(booker);

        // Создание тестовых объектов Booking
        booking1 = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();
        entityManager.persist(booking1);

        booking2 = Booking.builder()
                .item(item)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(3))
                .status(BookingStatus.WAITING)
                .build();
        entityManager.persist(booking2);

        entityManager.flush();
    }

    @Test
    @DirtiesContext
    void findAllByBookerIdOrderByStartDesc_ShouldReturnBookings() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(2L);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerIdOrderByStartDesc_ShouldReturnBookings() {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(item.getOwnerId());
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        log.info("Бронирование 2 {} и результат {}", booking2.getId(), bookings.get(0).getId());
        assertEquals(booking2.getId(), bookings.get(0).getId());
    }

    @Test
    @DirtiesContext
    void findFirstByItemIdAndEndBeforeOrderByEndDesc_ShouldReturnLastPastBooking() {
        List<Booking> allBookings = bookingRepository.findAll();
        log.info("Все бронирования: {}", allBookings);
        Booking result = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(
                item.getId(),
                LocalDateTime.now()
        );
        log.info("Бронирование 1 {} и результат {}", booking1.getId(), result.getId());
        assertNotNull(result);
        assertEquals(booking1.getId(), result.getId());
    }

    @Test
    @DirtiesContext
    void findFirstByItemIdAndStartAfterOrderByStartAsc_ShouldReturnNextFutureBooking() {
        Booking result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                item.getId(),
                LocalDateTime.now().plusHours(1)
        );

        assertNotNull(result);
        assertEquals(booking2.getId(), result.getId());
    }

    @Test
    @DirtiesContext
    void findAllByItemOwnerIdAndStartAfterOrderByStartDesc_ShouldReturnFutureBookings() {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                1L,
                LocalDateTime.now().minusHours(1)
        );

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
    }

    @Test
    @DirtiesContext
    void findAllByBookerIdAndEndBeforeOrderByStartDesc_ShouldReturnPastBookings() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                2L,
                LocalDateTime.now().plusDays(1)
        );

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getId(), bookings.get(0).getId());
    }

    @Test
    @DirtiesContext
    void existsByItemIdAndStatusAndStartBeforeAndEndAfter_ShouldReturnTrue() {
        boolean exists = bookingRepository.existsByItemIdAndStatusAndStartBeforeAndEndAfter(
                item.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.now().plusDays(2), // "Сейчас + 2 days" как конец периода
                LocalDateTime.now().minusDays(3) // "Сейчас - 3 days" как начало периода
        );

        assertTrue(exists, "Бронирование должно существовать для указанных условий");
    }
}
