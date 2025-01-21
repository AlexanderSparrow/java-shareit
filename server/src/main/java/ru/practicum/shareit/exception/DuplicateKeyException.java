package ru.practicum.shareit.exception;

public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException(final String message) {
        super(message);
    }
}
