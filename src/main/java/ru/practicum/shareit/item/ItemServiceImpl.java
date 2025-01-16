package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(long itemId, long userId) {
        // Получаем вещь или выбрасываем исключение, если она не найдена
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));

        // Получаем комментарии к вещи
        List<Comment> comments = commentRepository.findByItemId(itemId);

        // Если пользователь владелец, добавляем информацию о бронированиях
        boolean isOwner = Objects.equals(item.getOwnerId(), userId);
        Booking lastBooking = null;
        Booking nextBooking = null;

        if (isOwner) {
            lastBooking = bookingRepository.findLastBooking(itemId, LocalDateTime.now());
            nextBooking = bookingRepository.findNextBooking(itemId, LocalDateTime.now());
        }

        // Создаем DTO с учетом владельца
        return ItemResponseMapper.toItemResponseDto(item, comments, lastBooking, nextBooking);
    }

    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public void deleteItem(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (item.getOwnerId() != userId) {
            throw new AccessDeniedException("Пользователь не владеет этой вещью");
        }

        itemRepository.delete(item);
    }

    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));

        if (item.getOwnerId() != userId) {
            throw new NotFoundException("Пользователь не владеет этой вещью");
        }

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository.searchItems(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not Found"));
        User user = userRepository.findById(userId)
                .orElseThrow((() -> new NotFoundException("User not Found")));
        List<BookingResponseDto> allByBookerId = bookingService.getUserBookings(userId, "PAST");

        boolean b = allByBookerId.stream()
                .anyMatch(f -> f.getItem().getId() == itemId);
        if (!b) {
            throw new ValidationException("Booking for userId not found");
        }

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment save = commentRepository.save(comment);
        return CommentMapper.toCommentResponseDto(save);
    }

}
