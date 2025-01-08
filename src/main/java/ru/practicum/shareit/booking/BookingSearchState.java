package ru.practicum.shareit.booking;
import ru.practicum.shareit.exception.ValidationException;


public enum BookingSearchState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingSearchState from(String value) {
        try {
            return BookingSearchState.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Неизвестный статус: " + value);
        }
    }
}
