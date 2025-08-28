package com.app.controller;

import com.app.entities.Event;
import com.app.service.EventServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/events")
public class EventController {
	@Autowired
    private EventServiceImpl eventService;
    

    @GetMapping
    public ResponseEntity<List<Event>> list() {
        return ResponseEntity.ok(eventService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> get(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.get(id));
    }

    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.create(event));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Event> update(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.update(id, event));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.ok().body("Deleted");
    }
}
