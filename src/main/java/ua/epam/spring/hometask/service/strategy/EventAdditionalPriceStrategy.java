package ua.epam.spring.hometask.service.strategy;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

public interface EventAdditionalPriceStrategy {
    
    double getAdditionaPrice(Event event, Auditorium auditorium, long seat);
    
}
