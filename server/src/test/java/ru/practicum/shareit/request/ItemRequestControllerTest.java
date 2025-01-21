package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ItemRequestController requestController;

    @Mock
    private ItemRequestService requestService;

    private ObjectMapper objectMapper;

    private ItemRequestDto requestDto;
    private ItemRequestWithResponsesDto requestWithResponsesDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
        requestDto = new ItemRequestDto(1L, "Test description", 2L, Instant.now());
        requestWithResponsesDto = new ItemRequestWithResponsesDto(1L, "Test description", Instant.now(), Collections.emptyList());
    }

    @Test
    void createNewRequest_shouldReturnCreatedRequest() throws Exception {
        when(requestService.createNewRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$.requestorId").value(requestDto.getRequestorId()));
    }

    @Test
    void findAllRequestsByUserWithResponses_shouldReturnRequests() throws Exception {
        when(requestService.findAllRequestsByUserWithResponses(anyLong())).thenReturn(List.of(requestWithResponsesDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestWithResponsesDto.getId()))
                .andExpect(jsonPath("$[0].description").value(requestWithResponsesDto.getDescription()));
    }

    @Test
    void getAllRequests_shouldReturnRequests() throws Exception {
        when(requestService.getAllRequests(anyLong())).thenReturn(List.of(requestWithResponsesDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestWithResponsesDto.getId()))
                .andExpect(jsonPath("$[0].description").value(requestWithResponsesDto.getDescription()));
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(requestWithResponsesDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestWithResponsesDto.getId()))
                .andExpect(jsonPath("$.description").value(requestWithResponsesDto.getDescription()));
    }
}
