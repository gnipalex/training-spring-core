package ua.epam.spring.hometask.dao;

import java.awt.Event;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import ua.epam.spring.hometask.domain.Auditorium;

public interface AuditoriumDao {
    
    Set<Auditorium> getAll();
    
    Auditorium findByName(String name);
    
    Auditorium getByCode(String code);
    
    Map<LocalDateTime, Auditorium> getAuditoriumsForEvent(Event event);
    
}
