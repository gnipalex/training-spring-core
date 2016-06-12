package ua.epam.spring.hometask.service.strategy.impl;

import java.time.LocalDateTime;

import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

public class EveryXTicketDiscountStrategy implements DiscountStrategy {

    private int number;
    private TicketDao ticketDao;
    
    @Override
    public byte getDiscount(User user, Event event, LocalDateTime airDateTime,
            long numberOfTickets) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }
    
}
