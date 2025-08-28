package com.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.app.entities.Event;
import com.app.service.EventServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


@WebMvcTest(controllers = EventController.class,
excludeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = com.app.security.JWTRequestFilter.class
)
)
@AutoConfigureMockMvc(addFilters = false) // disable security filters for unit tests
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventServiceImpl eventService;

    @Test
    void testListEvents() throws Exception {
        Event e = new Event("Conf", "Mumbai", "Tech Conference", LocalDate.of(2025, 8, 28), 100);
        Mockito.when(eventService.list()).thenReturn(List.of(e));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Conf"))
                .andExpect(jsonPath("$[0].location").value("Mumbai"));
    }

    @Test
    void testGetEventById() throws Exception {
        Event e = new Event("Summit", "Delhi", "Business Summit", LocalDate.of(2025, 8, 29), 200);
        Mockito.when(eventService.get(1L)).thenReturn(e);

        mockMvc.perform(get("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Summit"))
                .andExpect(jsonPath("$.capacity").value(200));
    }

    @Test
    void testCreateEvent() throws Exception {
        Event e = new Event("Meetup", "Pune", "Community Meetup", LocalDate.of(2025, 9, 1), 50);
        Mockito.when(eventService.create(any(Event.class))).thenReturn(e);

        String requestBody = """
            {
                "name":"Meetup",
                "location":"Pune",
                "description":"Community Meetup",
                "eventDate":"2025-09-01",
                "capacity":50
            }
        """;

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Meetup"))
                .andExpect(jsonPath("$.location").value("Pune"));
    }

    @Test
    void testUpdateEvent() throws Exception {
        Event e = new Event("Updated Meetup", "Pune", "Updated Desc", LocalDate.of(2025, 9, 1), 60);
        Mockito.when(eventService.update(eq(1L), any(Event.class))).thenReturn(e);

        String requestBody = """
            {
                "name":"Updated Meetup",
                "location":"Pune",
                "description":"Updated Desc",
                "eventDate":"2025-09-01",
                "capacity":60
            }
        """;

        mockMvc.perform(put("/api/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Meetup"))
                .andExpect(jsonPath("$.capacity").value(60));
    }

    @Test
    void testDeleteEvent() throws Exception {
        Mockito.doNothing().when(eventService).delete(1L);

        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted"));
    }
}
