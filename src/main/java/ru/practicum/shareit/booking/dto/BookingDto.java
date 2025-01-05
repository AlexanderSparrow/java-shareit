package ru.practicum.shareit.booking.dto;


import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long itemId;
    long bookerId;
    BookingStatus status;
}
