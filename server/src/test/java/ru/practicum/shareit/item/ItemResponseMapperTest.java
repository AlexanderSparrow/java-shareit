package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingResponseMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemResponseMapperTest {

    @Test
    void toShortItemResponseDto_createsCorrectDto() {
        Long itemId = 1L;
        String itemName = "Test Item";

        ItemShortResponseDto dto = ItemResponseMapper.toShortItemResponseDto(itemId, itemName);

        assertNotNull(dto);
        assertEquals(itemId, dto.getId());
        assertEquals(itemName, dto.getName());
    }

    @Test
    void toItemResponseDto_createsCorrectDtoWithAllData() {
        // Arrange
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        List<Comment> comments = List.of(comment);

        Booking lastBooking = new Booking();
        lastBooking.setId(1L);

        Booking nextBooking = new Booking();
        nextBooking.setId(2L);

        BookingResponseDto lastBookingDto = mock(BookingResponseDto.class);
        BookingResponseDto nextBookingDto = mock(BookingResponseDto.class);
        CommentResponseDto commentDto = mock(CommentResponseDto.class);

        mockStatic(BookingResponseMapper.class);
        when(BookingResponseMapper.toBookingResponseDto(lastBooking)).thenReturn(lastBookingDto);
        when(BookingResponseMapper.toBookingResponseDto(nextBooking)).thenReturn(nextBookingDto);

        mockStatic(CommentMapper.class);
        when(CommentMapper.toCommentResponseDto(comment)).thenReturn(commentDto);

        // Act
        ItemResponseDto dto = ItemResponseMapper.toItemResponseDto(item, comments, lastBooking, nextBooking);

        // Assert
        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertTrue(dto.getAvailable());

        assertEquals(1, dto.getComments().size());
        assertEquals(commentDto, dto.getComments().get(0));

        assertEquals(lastBookingDto, dto.getLastBooking());
        assertEquals(nextBookingDto, dto.getNextBooking());
    }

    @Test
    void toItemResponseDto_createsCorrectDtoWithoutBookings() {
        // Arrange
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        List<Comment> comments = List.of();

        // Act
        ItemResponseDto dto = ItemResponseMapper.toItemResponseDto(item, comments, null, null);

        // Assert
        assertNotNull(dto);
        assertEquals(item.getId(), dto.getId());
        assertEquals(item.getName(), dto.getName());
        assertEquals(item.getDescription(), dto.getDescription());
        assertTrue(dto.getAvailable());

        assertTrue(dto.getComments().isEmpty());
        assertNull(dto.getLastBooking());
        assertNull(dto.getNextBooking());
    }

    @Test
    void toItemResponseDto_handlesNullItemGracefully() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                ItemResponseMapper.toItemResponseDto(null, List.of(), null, null));
    }
}
