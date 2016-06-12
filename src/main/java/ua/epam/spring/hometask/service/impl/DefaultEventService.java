package ua.epam.spring.hometask.service.impl;

import java.util.Collection;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.EventService;

public class DefaultEventService implements EventService {

    private EventDao eventDao;
    
    @Override
    public Event save(Event object) {
        return eventDao.save(object);
    }

    @Override
    public void remove(Event object) {
        eventDao.remove(object);
    }

    @Override
    public Event getById(long id) {
        return eventDao.getById(id);
    }

    @Override
    public Collection<Event> getAll() {
        return eventDao.getAll();
    }

    @Override
    public Event getByName(String name) {
        return eventDao.getByName(name);
    }

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
    
}
