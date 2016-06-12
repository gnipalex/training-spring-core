package ua.epam.spring.hometask.service.strategy.impl;

import java.time.LocalDateTime;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

public class BirthdayDiscountStrategy implements DiscountStrategy {

    @Override
    public byte getDiscount(User user, Event event, LocalDateTime airDateTime,
            long numberOfTickets) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    

}
