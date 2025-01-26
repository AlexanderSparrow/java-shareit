package ru.practicum.shareit.booking.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.user.dto.BookerDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemShortResponseDto item;
    private BookerDto booker;
    private BookingStatus status;
}
