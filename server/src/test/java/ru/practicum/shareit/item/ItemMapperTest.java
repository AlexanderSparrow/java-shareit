package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemMapperTest {

    @Test
    void toItemDto_shouldMapCorrectly() {
        // Arrange
        Item item = new Item(1L, 2L, "Item1", "Description", true, 1L);

        // Act
        ItemDto result = ItemMapper.toItemDto(item);

        // Assert
        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getOwnerId(), result.getOwnerId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(item.getRequestId(), result.getRequestId());
    }

    @Test
    void toItem_shouldMapCorrectly() {
        // Arrange
        ItemDto itemDto = new ItemDto(1L, 2L, "Item1", "Description", true, 1L);

        // Act
        Item result = ItemMapper.toItem(itemDto);

        // Assert
        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getOwnerId(), result.getOwnerId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        assertEquals(itemDto.getRequestId(), result.getRequestId());
    }
}

