package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Column(name = "start_date", nullable = false)
    Instant startDate;
    @Column(name = "end_date", nullable = false)
    Instant endDate;
    @Column(name = "item_id", nullable = false)
    long itemId;
    @Column(name = "booker_id", nullable = false)
    long booker_id;
    @Column(name = "status")
    BookingStatus status;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
}