package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.Event;

public interface EventDao extends DomainObjectDao<Event> {
    
    Event getByName(String name);
    
}
