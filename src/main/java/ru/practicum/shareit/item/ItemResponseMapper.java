package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.BookingResponseMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemResponseMapper {

    public ItemShortResponseDto toShortItemResponseDto(Long itemId, String itemName) {
        return new ItemShortResponseDto(itemId, itemName);
    }

    public ItemResponseDto toItemResponseDto(Item item, List<Comment> comments, Booking lastBooking, Booking nextBooking) {
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());

        dto.setComments(comments.stream()
                .map(CommentMapper::toCommentResponseDto)
                .collect(Collectors.toList()));

        if (lastBooking != null) {
            dto.setLastBooking(BookingResponseMapper.toBookingResponseDto(lastBooking, item.getName()));
        }
        if (nextBooking != null) {
            dto.setNextBooking(BookingResponseMapper.toBookingResponseDto(nextBooking, item.getName()));
        }

        return dto;
    }
}
