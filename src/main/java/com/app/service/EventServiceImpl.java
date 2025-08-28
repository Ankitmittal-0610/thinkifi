package com.app.service;

import com.app.entities.Event;
import com.app.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService{
    @Autowired
	private EventRepository repo;
    
    @Override
    public Event create(Event e) { return repo.save(e); }
    
    @Override
    public Event update(Long id, Event e) {
        Event exist = repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        exist.setName(e.getName());
        exist.setLocation(e.getLocation());
        exist.setEventDate(e.getEventDate());
        exist.setDescription(e.getDescription());
        exist.setCapacity(e.getCapacity());
        return repo.save(exist);
    }
    
    @Override
    public Event get(Long id) { 
    	return repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found")); 
	}
    
    @Override
    public List<Event> list() { 
    	return repo.findAll(); 
    }
    
    @Override
    public void delete(Long id) { 
    	repo.deleteById(id); 
    }
}
