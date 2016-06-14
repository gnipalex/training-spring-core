package ua.epam.spring.hometask.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.domain.Event;

public class InMemoryEventDao implements EventDao {

	private List<Event> events = new ArrayList<>();
	
	private IdGenerator idGenerator;
	
	@Override
	public Event save(Event event) {
		Event savedEvent = null;
		if (eventExists(event)) {
			savedEvent = getOriginalEvent(event).get();
		} else {
			savedEvent = new Event();
			savedEvent.setId(idGenerator.generateNextId());
			events.add(savedEvent);
		}
 		return null;
	}

	private Optional<Event> getOriginalEvent(Event event) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean eventExists(Event event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(Event object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Event getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Event> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event getByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
