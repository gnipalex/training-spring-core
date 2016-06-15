package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.strategy.EventAdditionalPriceStrategy;

public class DefaultBookingService implements BookingService {

    private TicketDao ticketDao;
    private DiscountService discountService;
    private EventAdditionalPriceStrategy additionalPriceStrategy;
    private EventDao eventDao;
    
    @Override
    public double getTicketsPrice(Event event, LocalDateTime dateTime,
            User user, Set<Long> seats) {
        
        double totalPrice = getPriceForSeats(event, dateTime, seats);
        
        double totalDiscounts = discountService.getDiscount(user, event, dateTime, seats.size());
        
        return totalPrice;
    }
    
    
     
    private double getPriceForSeats(Event event, LocalDateTime dateTime, Set<Long> seats) {
    	Auditorium auditorium = getAuditoriumByEventAndDate(event, dateTime);	
    	checkIfAllSeatsExistInAuditorium(auditorium, seats);
    	return seats.stream().map(seat -> getPrice(auditorium, event, seat))
    			.mapToDouble(Double::valueOf).sum();
	}
    
    private double getPrice(Auditorium auditorium, Event event, long seat) {
    	double basePrice = event.getBasePrice();
    	double additionalPrice = additionalPriceStrategy.getAdditionaPrice(event, auditorium, seat);
    	return basePrice + additionalPrice;
    }

    private void checkIfAllSeatsExistInAuditorium(Auditorium auditorium, Set<Long> seats) {
    	if (!auditorium.getAllSeats().containsAll(seats)) {
    		throw new IllegalArgumentException("auditorium for the event does not contain all specified seats"); 
    	}
    }

	private Auditorium getAuditoriumByEventAndDate(Event event, LocalDateTime dateTime) {
    	Map<LocalDateTime, Auditorium> auditoriums = eventDao.getAuditoriumAssignments(event);
    	Auditorium auditorium = auditoriums.get(dateTime);
    	if (auditorium == null) {
            throw new IllegalArgumentException("event does not present in any auditorium for time " + dateTime);
        }
    	return auditorium;
    }

//    private double getBasePrice(Event event, LocalDateTime dateTime, Set<Long> seats) {
//        Auditorium auditorium = event.getAuditoriums().get(dateTime);
//        double price = additionalPriceStrategy.getPrice(event, auditorium, seats) + ordinarySeatsPriceStrategy.getPrice(event, auditorium, seats);
//        return price;
//    }
//    
//    

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
    
    
    
	@Override
	public boolean doesBookingPresent(Event event, LocalDateTime dateTime, long seat) {
		Set<Ticket> tickets = ticketDao.getTicketsForEventAndDateTime(event, dateTime);
		return tickets.stream().anyMatch(t -> Objects.equals(t.getSeat(), seat));
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
