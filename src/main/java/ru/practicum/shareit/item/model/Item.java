package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Item {
    private long id;
    private long ownerId;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;

}


