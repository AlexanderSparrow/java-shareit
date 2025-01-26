package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemByRequestResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemByRequestResponseDtoJsonTest {
    @Autowired
    private JacksonTester<ItemByRequestResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new ItemByRequestResponseDto(1L, "Saw", 2L);

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.ownerId");
        assertThat(result).extractingJsonPathValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathValue("$.ownerId").isEqualTo(dto.getOwnerId().intValue());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "id": 1,
                    "name": "Saw",
                    "ownerId": 2
                }
                """;

        var dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Saw");
        assertThat(dto.getOwnerId()).isEqualTo(2L);
    }
}
