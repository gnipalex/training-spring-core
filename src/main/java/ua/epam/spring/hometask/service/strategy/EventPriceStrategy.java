package ua.epam.spring.hometask.service.strategy;

import java.util.Set;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

public interface EventPriceStrategy {
    
    double getPrice(Event event, Auditorium auditorium, Set<Long> seats);
    
}
