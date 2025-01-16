package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getUserItems(long userId);

    ItemResponseDto getItemById(long itemId, long userId);

    ItemDto addNewItem(@Valid long userId, ItemDto itemDto);

    ItemDto updateItem(@Valid long userId, long itemId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);

    CommentResponseDto addComment(Long itemId, Long userId, @Valid CommentDto commentDto);

    void deleteItem(long userId, long itemId);
}
