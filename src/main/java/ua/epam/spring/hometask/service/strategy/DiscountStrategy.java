package ua.epam.spring.hometask.service.strategy;

import java.time.LocalDateTime;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

public interface DiscountStrategy {
    
    double getDiscount(User user, Event event, LocalDateTime airDateTime, long numberOfTickets);

}
