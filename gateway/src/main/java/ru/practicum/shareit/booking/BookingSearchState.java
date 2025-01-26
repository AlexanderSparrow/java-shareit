package ru.practicum.shareit.booking;


import java.util.Optional;

public enum BookingSearchState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<BookingSearchState> from(String stringState) {
        for (BookingSearchState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
