package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    private ObjectMapper objectMapper;

    private BookingDto bookingDto;
    private BookingResponseDto bookingResponseDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.parse("2025-01-10T10:00:00"));
        bookingDto.setEnd(LocalDateTime.parse("2025-01-11T10:00:00"));
        bookingDto.setBookerId(1L);

        bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStatus(BookingStatus.APPROVED);
    }

    @Test
    void createBooking_ShouldReturnCreatedBooking() throws Exception {
        Mockito.when(bookingService.createBooking(anyLong(), any(BookingDto.class)))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().toString()));
    }

    @Test
    void updateBookingStatus_ShouldReturnUpdatedBooking() throws Exception {
        Mockito.when(bookingService.updateBookingStatus(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().toString()));
    }

    @Test
    void getBookingById_ShouldReturnBooking() throws Exception {
        Mockito.when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingResponseDto.getStatus().toString()));
    }

    @Test
    void getUserBookings_ShouldReturnBookingList() throws Exception {
        Mockito.when(bookingService.getUserBookings(anyLong(), anyString()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponseDto.getStatus().toString()));
    }

    @Test
    void getOwnerBookings_ShouldReturnOwnerBookingList() throws Exception {
        Mockito.when(bookingService.getOwnerBookings(anyLong(), anyString()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(bookingResponseDto.getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponseDto.getStatus().toString()));
    }

 /*   @Test
    void createBooking_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        BookingDto invalidBookingDto = new BookingDto(); // пустой объект не пройдет валидацию

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookingDto)))
                .andExpect(status().isBadRequest());
    }*/
}
