package ru.practicum.shareit.handler;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import static org.mockito.Mockito.when;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ValidationException validationException;

    @Mock
    private NotFoundException notFoundException;

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/some-endpoint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invalidField\": \"value\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());
    }

    @Test
    void handleCustomValidationException_ShouldReturnBadRequest() throws Exception {
        when(validationException.getMessage()).thenReturn("Custom validation error");

        mockMvc.perform(MockMvcRequestBuilders.post("/some-endpoint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invalidField\": \"value\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Custom validation error"));
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound() throws Exception {
        when(notFoundException.getMessage()).thenReturn("Resource not found");

        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint/{id}", 123))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Resource not found"));
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint")
                        .param("invalidParam", "value"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());
    }

    @Test
    void handleAccessDeniedException_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists());
    }
}
