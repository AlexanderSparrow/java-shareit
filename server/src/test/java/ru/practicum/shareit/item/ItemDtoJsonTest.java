package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new ItemDto(1L, 2L, "Saw", "Tool", Boolean.TRUE, 2L);

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.ownerId");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.requestId");

        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathValue("$.ownerId").isEqualTo(dto.getOwnerId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(dto.getRequestId().intValue());
    }

}