package ua.epam.spring.hometask.service.strategy.impl;

import java.util.Set;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.strategy.EventPriceStrategy;

public class OrdinarySeatsEventPriceStrategy implements EventPriceStrategy {

    @Override
    public double getPrice(Event event, Auditorium auditorium, Set<Long> seats) {
        // TODO Auto-generated method stub
        return 0;
    }

    
    
}
