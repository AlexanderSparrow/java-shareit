package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private long id;
    private Instant start;
    private Instant end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
