package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@Transactional
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private EntityManager entityManager;

    private Item item1;
    private Item item2;
    private User owner;

    @BeforeEach
    void setUp() {
        // Создаем владельца
        owner = User.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .build();
        entityManager.persist(owner);

        // Создаем первый предмет
        item1 = Item.builder()
                .name("Test Item 1")
                .description("This is a test item 1")
                .available(true)
                .ownerId(owner.getId())
                .build();
        entityManager.persist(item1);

        // Создаем второй предмет
        item2 = Item.builder()
                .name("Test Item 2")
                .description("This is a test item 2")
                .available(false)
                .ownerId(owner.getId())
                .build();
        entityManager.persist(item2);

        entityManager.flush();
    }

    @Test
    @DirtiesContext
    void findByOwnerId_ShouldReturnItems() {
        List<Item> items = itemRepository.findByOwnerId(owner.getId());

        assertNotNull(items);
        assertEquals(2, items.size());
        assertTrue(items.stream().anyMatch(item -> item.getName().equals("Test Item 1")));
        assertTrue(items.stream().anyMatch(item -> item.getName().equals("Test Item 2")));
    }

    @Test
    @DirtiesContext
    void findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase_ShouldReturnMatchingItems() {
        List<Item> items = itemRepository.findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(
                "item", "test");

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item1.getId(), items.get(0).getId());
    }

    @Test
    @DirtiesContext
    void existsByIdAndOwnerId_ShouldReturnTrueIfExists() {
        boolean exists = itemRepository.existsByIdAndOwnerId(item1.getId(), owner.getId());

        assertTrue(exists);
    }

    @Test
    @DirtiesContext
    void existsByIdAndOwnerId_ShouldReturnFalseIfNotExists() {
        boolean exists = itemRepository.existsByIdAndOwnerId(999L, owner.getId());

        assertFalse(exists);
    }

    @Test
    @DirtiesContext
    void findAllByRequestId_ShouldReturnItemsWithRequestId() {

        ItemRequest itemRequest = ItemRequest.builder()
                .requestor(owner)
                .description("Need a test item")
                .created(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
                .build();
        entityManager.persist(itemRequest);

        item1.setRequestId(1L);
        entityManager.persist(item1);
        entityManager.flush();

        List<Item> items = itemRepository.findAllByRequestId(1L);

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item1.getId(), items.get(0).getId());
    }

    @Test
    @DirtiesContext
    void findAllByRequestId_ShouldReturnEmptyListIfNoItemsFound() {
        List<Item> items = itemRepository.findAllByRequestId(999L);

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }
}
