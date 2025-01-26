package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> getUserItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> addNewItem(long userId, @Valid ItemDto itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> deleteItem(long userId, long itemId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemDto itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> addComment(Long itemId, Long userId, @Valid CommentDto commentDto) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}
