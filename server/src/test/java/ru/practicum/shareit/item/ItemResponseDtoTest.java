package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemResponseDtoTest {
    @Autowired
    private JacksonTester<ItemResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        var lastBooking = new BookingResponseDto(1L, LocalDateTime.now().minusHours(2), LocalDateTime.now().minusHours(1), null, null, null);
        var nextBooking = new BookingResponseDto(4L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), null, null, null);
        var comments = List.of(new CommentResponseDto(1L, "Good item", "John Doe", null));

        var dto = new ItemResponseDto(1L, "Saw", "Tool", true, lastBooking, nextBooking, comments);

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "id": 1,
                    "name": "Saw",
                    "description": "Tool",
                    "available": true,
                    "lastBooking": {"id": 1, "bookerId": 2, "itemId": 3},
                    "nextBooking": {"id": 4, "bookerId": 5, "itemId": 6},
                    "comments": [{"id": 1, "text": "Good item", "authorName": "John Doe"}]
                }
                """;

        var dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Saw");
        assertThat(dto.getDescription()).isEqualTo("Tool");
        assertThat(dto.getAvailable()).isTrue();
        assertThat(dto.getLastBooking().getId()).isEqualTo(1L);
        assertThat(dto.getNextBooking().getId()).isEqualTo(4L);
        assertThat(dto.getComments()).hasSize(1);
    }
}
