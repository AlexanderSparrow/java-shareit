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
}
