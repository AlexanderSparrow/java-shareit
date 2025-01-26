package ru.practicum.shareit.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null, null);
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, userId, parameters, null);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return post(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, T body) {
        return put(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path, long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PUT, path, userId, parameters, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId) {
        return patch(path, userId, null, null);
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return patch(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path, Long userId, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.PATCH, path, userId, parameters, body);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null, null);
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        return delete(path, userId, null);
    }

    protected ResponseEntity<Object> delete(String path, Long userId, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.DELETE, path, userId, parameters, null);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, Long userId,
                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
        log.info("HTTP Request - Method: {}, URI: {}, UserId: {}, Parameters: {}, Body: {}",
                method, path, userId, parameters, body);

        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));
        ResponseEntity<Object> shareitServerResponse;

        try {
            if (parameters != null) {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                shareitServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
            log.info("HTTP Response - Status: {}, Body: {}",
                    shareitServerResponse.getStatusCode(), shareitServerResponse.getBody());
        } catch (HttpStatusCodeException e) {

            log.error("HTTP Error - Status: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            log.error("Unexpected error during HTTP request: {}", e.getMessage(), e);
            throw e;
        }

        return prepareGatewayResponse(shareitServerResponse);
    }


    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }
}
