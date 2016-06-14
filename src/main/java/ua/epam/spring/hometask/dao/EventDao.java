package ua.epam.spring.hometask.dao;

import java.time.LocalDateTime;
import java.util.NavigableMap;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

public interface EventDao extends DomainObjectDao<Event> {
    
    Event getByName(String name);
    
    NavigableMap<LocalDateTime, Auditorium> getAuditoriumAssignments(Event event);
    
    void assignAuditorium(Event event, Auditorium auditorium, LocalDateTime dateTime);
    
    boolean removeAuditoriumAssignment(Event event, LocalDateTime dateTime);
    
    boolean removeAirDateTime(Event event, LocalDateTime dateTime);
    
    
    
}
