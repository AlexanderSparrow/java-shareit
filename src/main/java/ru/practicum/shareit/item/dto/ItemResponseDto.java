package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemResponseDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
    private List<CommentResponseDto> comments;
}
