package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .build();
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    @DirtiesContext
    void save_ShouldPersistUser() {
        User newUser = User.builder()
                .name("Jane Smith")
                .email("janesmith@example.com")
                .build();

        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId());
        assertEquals("Jane Smith", savedUser.getName());
        assertEquals("janesmith@example.com", savedUser.getEmail());
    }

    @Test
    @DirtiesContext
    void findById_ShouldReturnUser() {
        Optional<User> foundUser = userRepository.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals(user.getName(), foundUser.get().getName());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @DirtiesContext
    void findAll_ShouldReturnAllUsers() {
        User anotherUser = User.builder()
                .name("Jane Smith")
                .email("janesmith@example.com")
                .build();
        entityManager.persist(anotherUser);
        entityManager.flush();

        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    @DirtiesContext
    void deleteById_ShouldRemoveUser() {
        userRepository.deleteById(user.getId());

        Optional<User> deletedUser = userRepository.findById(user.getId());

        assertFalse(deletedUser.isPresent());
    }
}
