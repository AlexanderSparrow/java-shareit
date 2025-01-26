package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.dto.ItemResponseShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toItemRequestDto_shouldMapCorrectly() {
        // Arrange
        User requestor = new User(1L, "User1", "user1@example.com");
        ItemRequest request = new ItemRequest(1L, "Description", requestor, Instant.now());

        // Act
        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getRequestor().getId(), result.getRequestorId());
        assertEquals(request.getCreated(), result.getCreated());
    }

    @Test
    void toItemRequestDto_withNullRequest_shouldReturnNull() {
        // Act
        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(null);

        // Assert
        assertNull(result, "Expected null when input request is null");
    }

    @Test
    void toItemRequestWithResponsesDto_shouldMapCorrectly() {
        // Arrange
        User requestor = new User(1L, "User1", "user1@example.com");
        ItemRequest request = new ItemRequest(1L, "Description", requestor, Instant.now());
        ItemResponseShortDto itemResponseShortDto = new ItemResponseShortDto(1L, "Item1", 2L);

        // Act
        ItemRequestWithResponsesDto result = ItemRequestMapper.toItemRequestWithResponsesDto(request, List.of(itemResponseShortDto));

        // Assert
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(1, result.getItems().size());
        assertEquals(itemResponseShortDto.getItemId(), result.getItems().get(0).getItemId());
    }

    @Test
    void toItemRequestWithResponsesDto_withEmptyResponses_shouldHandleGracefully() {
        // Arrange
        User requestor = new User(1L, "User1", "user1@example.com");
        ItemRequest request = new ItemRequest(1L, "Description", requestor, Instant.now());

        // Act
        ItemRequestWithResponsesDto result = ItemRequestMapper.toItemRequestWithResponsesDto(request, Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getCreated(), result.getCreated());
        assertTrue(result.getItems().isEmpty(), "Expected empty list of items");
    }

    @Test
    void toItemRequestWithResponsesDto_withNullRequest_shouldReturnNull() {
        // Act
        ItemRequestWithResponsesDto result = ItemRequestMapper.toItemRequestWithResponsesDto(null, Collections.emptyList());

        // Assert
        assertNull(result, "Expected null when input request is null");
    }

    @Test
    void toItemResponseShortDto_shouldMapCorrectly() {
        // Arrange
        Item item = new Item(1L, 2L, "ItemName", "ItemDescription", true, null);

        // Act
        ItemResponseShortDto result = ItemRequestMapper.toItemResponseShortDto(item);

        // Assert
        assertNotNull(result);
        assertEquals(item.getId(), result.getItemId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getOwnerId(), result.getOwnerId());
    }

    @Test
    void toItemResponseShortDto_withNullItem_shouldReturnNull() {
        // Act
        ItemResponseShortDto result = ItemRequestMapper.toItemResponseShortDto(null);

        // Assert
        assertNull(result, "Expected null when input item is null");
    }
}
