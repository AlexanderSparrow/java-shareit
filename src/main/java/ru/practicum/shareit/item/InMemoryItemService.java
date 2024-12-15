package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InMemoryItemService implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null)
            throw new NotFoundException("Вещь с id:" + itemId + " не найдена!");
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        Item savedItem = itemRepository.addNewItem(userId, item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public void delete(long userId, long itemId) {
        itemRepository.delete(userId, itemId);

    }

    @Override
    public ItemDto partialUpdate(long userId, long itemId, Map<String, Object> updates) {
        Item updatedItem = itemRepository.partialUpdate(userId, itemId, updates);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItems(String text) {

        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
