package ua.epam.spring.hometask.dao;

import java.time.LocalDateTime;
import java.util.Set;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

public interface TicketDao extends DomainObjectDao<Ticket> {

    Set<Ticket> getTicketsForUser(User user);
    
    Set<Ticket> getTicketsForEventAndDateTime(Event event, LocalDateTime dateTime);
    
    boolean doesBookingExist(Ticket ticket);
    
}