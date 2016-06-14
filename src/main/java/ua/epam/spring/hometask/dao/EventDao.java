package ua.epam.spring.hometask.dao;

import java.time.LocalDateTime;
import java.util.NavigableMap;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

public interface EventDao extends DomainObjectDao<Event> {
    
    Event getByName(String name);
    
    NavigableMap<LocalDateTime, Auditorium> getAuditoriumAssignments(Event event);

}
