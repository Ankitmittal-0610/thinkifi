package com.app.service;

import java.util.List;

import com.app.entities.Event;

public interface EventService {
	Event create(Event e);
	Event update(Long id, Event e);
	Event get(Long id);
	List<Event> list() ;
	void delete(Long id);
	
}
