package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InMemoryItemService implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository; // Для проверки существования пользователей

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден!");
        }
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        validateUserExists(userId);
        return itemRepository.getUserItems(userId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с id: " + itemId + " не найдена!");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        validateUserExists(userId);

        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Наименование вещи не может быть пустым.");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Описание вещи не может быть пустым.");
        }

        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Доступность должна быть указана.");
        }

        Item item = ItemMapper.toItem(itemDto);
        Item savedItem = itemRepository.addNewItem(userId, item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public void delete(long userId, long itemId) {
        validateUserExists(userId);
        itemRepository.delete(userId, itemId);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        validateUserExists(userId);
        Item existingItem = itemRepository.getItemById(itemId);

        if (existingItem == null || existingItem.getOwnerId() != userId) {
            throw new NotFoundException("Вещь с id: " + itemId + " не найдена или не принадлежит пользователю.");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.updateItem(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItems(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }
}
