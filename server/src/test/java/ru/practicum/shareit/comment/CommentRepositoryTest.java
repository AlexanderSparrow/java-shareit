package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        // Создаем пользователей
        User author = userRepository.save(User.builder()
                .email("author@example.com")
                .name("Author")
                .build());

        User anotherUser = userRepository.save(User.builder()
                .email("another@example.com")
                .name("AnotherUser")
                .build());

        // Создаем вещи
        item1 = itemRepository.save(Item.builder()
                .ownerId(author.getId())
                .name("ItemName1")
                .description("Description1")
                .available(true)
                .build());

        item2 = itemRepository.save(Item.builder()
                .ownerId(anotherUser.getId())
                .name("ItemName2")
                .description("Description2")
                .available(true)
                .build());

        // Создаем комментарии
        commentRepository.save(Comment.builder()
                .text("Comment1")
                .item(item1)
                .author(author)
                .created(LocalDateTime.now())
                .build());

        commentRepository.save(Comment.builder()
                .text("Comment2")
                .item(item1)
                .author(anotherUser)
                .created(LocalDateTime.now())
                .build());

        commentRepository.save(Comment.builder()
                .text("Comment3")
                .item(item2)
                .author(author)
                .created(LocalDateTime.now())
                .build());
    }

    @Test
    void testFindByItemId() {
        List<Comment> comments = commentRepository.findByItemId(item1.getId());

        assertThat(comments).hasSize(2);
        assertThat(comments.stream().map(Comment::getText))
                .containsExactlyInAnyOrder("Comment1", "Comment2");
    }

    @Test
    void testFindAllByItemIdIn() {
        List<Comment> comments = commentRepository.findAllByItemIdIn(List.of(item1.getId(), item2.getId()));

        assertThat(comments).hasSize(3);
        assertThat(comments.stream().map(Comment::getText))
                .containsExactlyInAnyOrder("Comment1", "Comment2", "Comment3");
    }

    @Test
    void testFindByItemId_NoComments() {
        List<Comment> comments = commentRepository.findByItemId(999L);

        assertThat(comments).isEmpty();
    }
}
