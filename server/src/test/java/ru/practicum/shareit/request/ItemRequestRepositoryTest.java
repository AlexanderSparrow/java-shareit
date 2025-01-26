package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase
@Transactional
@DirtiesContext
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private EntityManager entityManager;

    private User user1;
    private User user2;
    private ItemRequest request1;
    private ItemRequest request2;

    @BeforeEach
    void setUp() {
        // Создание пользователей
        user1 = User.builder()
                .name("User One")
                .email("user1@example.com")
                .build();
        entityManager.persist(user1);

        user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .build();
        entityManager.persist(user2);

        // Создание запросов
        request1 = ItemRequest.builder()
                .requestor(user1)
                .description("Need a test item 1")
                .created(Instant.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)))
                .build();
        entityManager.persist(request1);

        request2 = ItemRequest.builder()
                .requestor(user2)
                .description("Need a test item 2")
                .created(Instant.from(LocalDateTime.now().minusHours(5).toInstant(ZoneOffset.UTC)))
                .build();
        entityManager.persist(request2);

        entityManager.flush();
    }

    @Test
    void findAllByOtherUsers_ShouldReturnRequestsFromOtherUsers() {
        List<ItemRequest> result = itemRequestRepository.findAllByOtherUsers(user1.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(request2.getId(), result.get(0).getId());
    }

    @Test
    void findByRequestorIdOrderByCreatedDesc_ShouldReturnRequestsForRequestor() {
        List<ItemRequest> result = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(user1.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(request1.getId(), result.get(0).getId());
    }


    @Test
    void findByRequestorIdOrderByCreatedDesc_ShouldReturnEmptyForNoRequests() {
        List<ItemRequest> result = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
