package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

@Slf4j
@Tag(name = "Items", description = "Управление вещами")
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на список вещей пользователя с id: {}.", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable long itemId) {
        log.info("Получен запрос на просмотр вещи с id: {} пользователем {}.", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавление новой вещи пользователем с id: {}. Детали: {}", userId, itemDto);
        return itemService.addNewItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId) {
        log.info("Удаление вещи с id: {} пользователем с id: {}.", itemId, userId);
        itemService.deleteItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable(name = "itemId") long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Обновление вещи с id: {} пользователем с id {}. Детали обновления: {}", itemId, userId, itemDto);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Поиск вещей по строке: '{}'.", text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(@PathVariable Long itemId,
                                         @Valid @RequestBody CommentDto commentDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Добавление комментария к вещи с id: {} пользователем с id: {}. Текст: '{}'.", itemId, userId, commentDto.getText());
        return itemService.addComment(itemId, userId, commentDto);
    }
}