package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentResponseDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Test User", "test@example.com");
        item = new Item(1L, 1L, "Item Name", "Item Description", true, user.getId());
    }

    @Test
    void getUserItems_ShouldReturnItems() {
        when(itemRepository.findByOwnerId(user.getId())).thenReturn(List.of(item));
        when(bookingRepository.findAllByItemIdInAndEndBeforeOrderByEndDesc(anyList(), any(LocalDateTime.class)))
                .thenReturn(List.of());

        List<ItemResponseDto> result = itemService.getUserItems(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(item.getName(), result.get(0).getName());

        verify(itemRepository, times(1)).findByOwnerId(user.getId());
    }

    @Test
    void getItemById_ItemExists_ShouldReturnItemResponseDto() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(anyLong(), any(LocalDateTime.class)))
                .thenReturn(null);

        ItemResponseDto result = itemService.getItemById(item.getId(), user.getId());

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
        verify(itemRepository, times(1)).findById(item.getId());
    }

    @Test
    void getItemById_ItemDoesNotExist_ShouldThrowNotFoundException() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(item.getId(), user.getId()));

        assertEquals("Вещь с id " + item.getId() + " не найдена", exception.getMessage());
    }

    @Test
    void addNewItem_ValidInput_ShouldSaveItem() {
        ItemDto itemDto = new ItemDto(0, 0, "Item Name", "Item Description", true, null); // Поля для добавления
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addNewItem(user.getId(), itemDto);

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }


    @Test
    void addNewItem_UserNotFound_ShouldThrowNotFoundException() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        ItemDto itemDto = new ItemDto(0, 0, "Item Name", "Item Description", true, null); // Поля для добавления
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.addNewItem(user.getId(), itemDto));

        assertEquals("Пользователь с id " + user.getId() + " не найден", exception.getMessage());
    }

    @Test
    void deleteItem_ItemOwnedByUser_ShouldDeleteItem() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        itemService.deleteItem(user.getId(), item.getId());

        verify(itemRepository, times(1)).delete(item);
    }

    @Test
    void deleteItem_ItemNotOwnedByUser_ShouldThrowAccessDeniedException() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> itemService.deleteItem(999L, item.getId()));

        assertEquals("Пользователь не владеет этой вещью", exception.getMessage());
    }

    @Test
    void addComment_ValidInput_ShouldSaveComment() {
        Comment comment = new Comment(1L, "Great item!", item, user, LocalDateTime.now());
        CommentDto commentDto = new CommentDto(0, "Great item!", null, null, null); // Указываем только текст комментария
        BookingResponseDto bookingResponseDto = new BookingResponseDto(
                1L,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(3),
                new ItemShortResponseDto(item.getId(), item.getName()),
                new BookerDto(user.getId()),
                BookingStatus.APPROVED
        );

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingService.getUserBookings(user.getId(), "PAST")).thenReturn(List.of(bookingResponseDto));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto result = itemService.addComment(item.getId(), user.getId(), commentDto);

        assertNotNull(result);
        assertEquals(comment.getText(), result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateItem_ItemExists_ShouldUpdateItem() {
        ItemDto itemDto = new ItemDto(1L, 1L, "Updated Name", "Updated Description", true, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.updateItem(user.getId(), item.getId(), itemDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem_ItemDoesNotExist_ShouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto(1L, 1L, "Updated Name", "Updated Description", true, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(user.getId(), item.getId(), itemDto));

        assertEquals("Вещь с id " + item.getId() + " не найдена", exception.getMessage());
    }

    @Test
    void updateItem_UserNotOwner_ShouldThrowNotFoundException() {
        ItemDto itemDto = new ItemDto(1L, 1L, "Updated Name", "Updated Description", true, null);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(999L, item.getId(), itemDto));

        assertEquals("Пользователь не владеет этой вещью", exception.getMessage());
    }

    @Test
    void searchItems_TextIsBlank_ShouldReturnEmptyList() {
        List<ItemDto> result = itemService.searchItems("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchItems_ItemsFound_ShouldReturnItemList() {
        Item item2 = new Item(2L, 1L, "Another Item", "Another Description", true, user.getId());
        when(itemRepository.findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase("Item", "Item")).thenReturn(List.of(item, item2));

        List<ItemDto> result = itemService.searchItems("Item");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(i -> i.getName().equals("Item Name")));
        assertTrue(result.stream().anyMatch(i -> i.getName().equals("Another Item")));
    }

    @Test
    void addComment_NoBookingFound_ShouldThrowValidationException() {
        CommentDto commentDto = new CommentDto(0, "Great item!", null, null, null); // Указываем только текст комментария

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingService.getUserBookings(user.getId(), "PAST")).thenReturn(List.of()); // Нет бронирования

        ValidationException exception = assertThrows(ValidationException.class,
                () -> itemService.addComment(item.getId(), user.getId(), commentDto));

        assertEquals("Бронирования для пользователя с id: " + user.getId() + " не найдено.", exception.getMessage());
    }

}
