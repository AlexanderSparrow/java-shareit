package ru.practicum.shareit.booking.dto;


import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.Instant;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    long id;
    Instant startDate;
    Instant endDate;
    long itemId;
    long bookerId;
    BookingStatus status;
}
