package com.app.service;

import com.app.entities.Event;
import com.app.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        event = new Event("Conf", "Mumbai", "Tech Conference", LocalDate.now(), 100);
    }

    @Test
    void testCreateEvent() {
        when(eventRepo.save(event)).thenReturn(event);
        Event created = eventService.create(event);
        assertEquals("Conf", created.getName());
        verify(eventRepo, times(1)).save(event);
    }

    @Test
    void testGetEvent() {
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        Event found = eventService.get(1L);
        assertEquals("Mumbai", found.getLocation());
        verify(eventRepo, times(1)).findById(1L);
    }

    @Test
    void testListEvents() {
        when(eventRepo.findAll()).thenReturn(Arrays.asList(event));
        assertEquals(1, eventService.list().size());
        verify(eventRepo, times(1)).findAll();
    }

    @Test
    void testUpdateEvent() {
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepo.save(any(Event.class))).thenReturn(event);
        Event updated = eventService.update(1L, event);
        assertEquals("Conf", updated.getName());
        verify(eventRepo, times(1)).save(event);
    }

    @Test
    void testDeleteEvent() {
        doNothing().when(eventRepo).deleteById(1L);
        eventService.delete(1L);
        verify(eventRepo, times(1)).deleteById(1L);
    }
}
