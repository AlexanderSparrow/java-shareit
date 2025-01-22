package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
                .ownerId(owner.getId()) // Используйте корректный ID владельца
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
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
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
    void existsByItemIdAndStatusAndStartBeforeAndEndAfter_ShouldReturnTrue() {
        boolean exists = bookingRepository.existsByItemIdAndStatusAndStartBeforeAndEndAfter(
                item.getId(),
                BookingStatus.APPROVED,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().minusHours(1)
        );

        assertTrue(exists);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc_ShouldReturnBookings() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(2L);

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(booking2.getId(), bookings.get(0).getId());
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc_ShouldReturnBookings() {
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(item.getOwnerId());

        assertNotNull(bookings);
        assertEquals(2, bookings.size()); // Убедитесь, что ожидаемое количество совпадает
        assertEquals(booking2.getId(), bookings.get(0).getId()); // Проверьте порядок
    }


    @Transactional
    @Test
    void findFirstByItemIdAndStartAfterOrderByStartAsc_ShouldReturnNextFutureBooking() {
        Booking result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                item.getId(),
                LocalDateTime.now().plusHours(1)
        );

        assertNotNull(result);
        assertEquals(booking2.getId(), result.getId());
    }
}
