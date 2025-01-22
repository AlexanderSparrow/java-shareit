package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
    List<ItemResponseDto> getUserItems(long userId);

    ItemResponseDto getItemById(long itemId, long userId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);

    CommentResponseDto addComment(Long itemId, Long userId, CommentDto commentDto);

    void deleteItem(long userId, long itemId);
}
