package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "test@example.com", "Test User");
        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Need a drill");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(Instant.now());

        requestDto = new ItemRequestDto();
        requestDto.setDescription("Need a drill");
    }

    @Test
    void createNewRequest_ShouldSaveRequest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = requestService.createNewRequest(user.getId(), requestDto);

        assertNotNull(result);
        //assertEquals(itemRequest.getId(), result.getId());
        assertEquals(itemRequest.getDescription(), result.getDescription());
        verify(userRepository, times(1)).findById(user.getId());
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void createNewRequest_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                requestService.createNewRequest(user.getId(), requestDto));

        assertEquals("Пользователь с id: " + user.getId() + " не найден.", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verifyNoInteractions(requestRepository);
    }

    @Test
    void findAllRequestsByUserWithResponses_ShouldReturnRequestsWithResponses() {
        when(requestRepository.findByRequestorIdOrderByCreatedDesc(user.getId()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(Collections.emptyList());

        List<ItemRequestWithResponsesDto> result = requestService.findAllRequestsByUserWithResponses(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getDescription(), result.getFirst().getDescription());
        verify(requestRepository, times(1)).findByRequestorIdOrderByCreatedDesc(user.getId());
    }

    @Test
    void getAllRequests_ShouldReturnRequests() {
        when(requestRepository.findAllByOtherUsers(user.getId())).thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(Collections.emptyList());

        List<ItemRequestWithResponsesDto> result = requestService.getAllRequests(user.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemRequest.getDescription(), result.getFirst().getDescription());
        verify(requestRepository, times(1)).findAllByOtherUsers(user.getId());
    }

    @Test
    void getRequestById_ShouldReturnRequestWithResponses() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(itemRequest.getId())).thenReturn(Collections.emptyList());

        ItemRequestWithResponsesDto result = requestService.getRequestById(user.getId(), itemRequest.getId());

        assertNotNull(result);
        assertEquals(itemRequest.getDescription(), result.getDescription());
        verify(requestRepository, times(1)).findById(itemRequest.getId());
        verify(itemRepository, times(1)).findAllByRequestId(itemRequest.getId());
    }

    @Test
    void getRequestById_RequestNotFound_ShouldThrowException() {
        when(requestRepository.findById(itemRequest.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                requestService.getRequestById(user.getId(), itemRequest.getId()));

        assertEquals("Запрос с id: " + itemRequest.getId() + " не найден.", exception.getMessage());
        verify(requestRepository, times(1)).findById(itemRequest.getId());
        verifyNoInteractions(itemRepository);
    }
}
