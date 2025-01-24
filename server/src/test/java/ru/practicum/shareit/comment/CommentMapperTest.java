package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    @Test
    void testToComment() {
        // Arrange
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Test comment")
                .created(LocalDateTime.now())
                .build();

        // Act
        Comment comment = CommentMapper.toComment(commentDto);

        // Assert
        assertThat(comment.getId()).isEqualTo(commentDto.getId());
        assertThat(comment.getText()).isEqualTo(commentDto.getText());
        assertThat(comment.getCreated()).isEqualTo(commentDto.getCreated());
    }

    @Test
    void testToCommentResponseDto() {
        // Arrange
        User author = User.builder()
                .id(1L)
                .name("AuthorName")
                .email("author@example.com")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("ItemName")
                .description("ItemDescription")
                .available(true)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Test comment")
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        // Act
        CommentResponseDto responseDto = CommentMapper.toCommentResponseDto(comment);

        // Assert
        assertThat(responseDto.getId()).isEqualTo(comment.getId());
        assertThat(responseDto.getText()).isEqualTo(comment.getText());
        assertThat(responseDto.getAuthorName()).isEqualTo(author.getName());
        assertThat(responseDto.getCreated()).isEqualTo(comment.getCreated());
    }
}
