package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.strategy.EventPriceStrategy;

public class DefaultBookingService implements BookingService {

    private TicketDao ticketDao;
    private DiscountService discountService;
    private EventPriceStrategy vipSeatsPriceStrategy;
    private EventPriceStrategy ordinarySeatsPriceStrategy;
    
    @Override
    public double getTicketsPrice(Event event, LocalDateTime dateTime,
            User user, Set<Long> seats) {
        checkIfEventHasSlotForDateTime(event, dateTime);
        double totalPrice = 0D;
        totalPrice += getBasePrice(event, dateTime, seats);
        // wrong discount calculation
        totalPrice -= discountService.getDiscount(user, event, dateTime, seats.size());
        return totalPrice;
    }
    
    private void checkIfEventHasSlotForDateTime(Event event, LocalDateTime dateTime) {
        Auditorium auditorium = event.getAuditoriums().get(dateTime);
        if (auditorium == null) {
            throw new IllegalArgumentException("event does not present for time " + dateTime);
        }
    }

    private double getBasePrice(Event event, LocalDateTime dateTime, Set<Long> seats) {
        Auditorium auditorium = event.getAuditoriums().get(dateTime);
        double price = vipSeatsPriceStrategy.getPrice(event, auditorium, seats) + ordinarySeatsPriceStrategy.getPrice(event, auditorium, seats);
        return price;
    }

    @Override
    public void bookTickets(Set<Ticket> tickets) {
        checkIfAllSeatsAreFree(tickets);
        tickets.forEach(ticket -> ticketDao.save(ticket));
    }
    
    private void checkIfAllSeatsAreFree(Set<Ticket> tickets) {
        Set<Ticket> conflictTickets = tickets.stream().filter(ticketDao::doesBookingExist).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(conflictTickets)) {
            throw new IllegalArgumentException("some of the bookings are not free");
        }
    }

//    private void checkIfSeatIsFree(Ticket ticket) {
//        Event event = ticket.getEvent();
//        LocalDateTime dateTime = ticket.getDateTime();
//        Set<Ticket> bookedTickets = ticketDao.getTicketsForEventAndDateTime(event, dateTime);
//        if (bookedTickets.contains(ticket.getSeat())) {
//            throw new IllegalStateException(getSeatIsBookedErrorMessage(ticket));
//        }
//    }
//    
//    private String getSeatIsBookedErrorMessage(Ticket ticket) {
//        DateFormat dateFormat = DateFormat.getDateTimeInstance();
//        return String.format("seat %d for event '%s' on date '%s' is already booked", ticket.getSeat(), 
//                ticket.getEvent().getName(), dateFormat.format(ticket.getDateTime()));
//    }

    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(Event event,
            LocalDateTime dateTime) {
        return ticketDao.getTicketsForEventAndDateTime(event, dateTime);
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public void setDiscountService(DiscountService discountService) {
        this.discountService = discountService;
    }
    
}
