package ru.practicum.shareit.item.model;

import lombok.Data;


@Data
public class Item {
    private long id;
    private long ownerId;
    private String name;
    private String description;
    private boolean available;
    private String request;
}

