package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.OrderService;
import ua.epam.spring.hometask.service.strategy.EventAdditionalPriceStrategy;

@Service("bookingService")
public class DefaultBookingService implements BookingService {

    @Resource
    private TicketDao ticketDao;
    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private EventDao eventDao;
    @Resource
    private DiscountService discountService;
    @Resource(name = "summingAdditionalEventPriceStrategy")
    private EventAdditionalPriceStrategy additionalPriceStrategy;
    @Resource
    private OrderService orderService;
    
    @Override
    public double getTicketsPrice(Event event, LocalDateTime dateTime,
            User user, Set<Long> seats) {
        double totalPrice = getPriceForSeats(event, dateTime, seats);
        double totalDiscounts = getDiscount(event, dateTime, user, seats);
        return totalPrice - totalDiscounts;
    }

	private double getDiscount(Event event, LocalDateTime dateTime, User user,
			Set<Long> seats) {
		return discountService.getDiscount(user, event, dateTime, seats.size());
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

    @Override
    public Order bookTickets(User user, Set<Ticket> tickets) {
        checkIfAllSeatsAreFree(tickets);

        Map<Long, Map<LocalDateTime, List<Ticket>>> ticketsGroupedByEventAndDate = groupByEventAndDate(tickets);
        
        List<OrderEntry> createdOrderEntries = new ArrayList<>();
        
        for (Entry<Long, Map<LocalDateTime, List<Ticket>>> ticketsGroupedByEvent : ticketsGroupedByEventAndDate.entrySet()) {
        	Event event = eventDao.getById(ticketsGroupedByEvent.getKey()); 
        	
        	for (Entry<LocalDateTime, List<Ticket>> ticketsGroupedByDate : ticketsGroupedByEvent.getValue().entrySet()) {
        		OrderEntry newOrderEntry = createOrderEntry();
        		
        		Set<Ticket> savedTickets = saveTickets(ticketsGroupedByDate.getValue(), newOrderEntry);

        		Set<Long> seats = savedTickets.stream().map(Ticket::getSeat).collect(Collectors.toSet());
        		LocalDateTime dateTime = ticketsGroupedByDate.getKey();
        		newOrderEntry.setBasePrice(getPriceForSeats(event, dateTime, seats));
        		newOrderEntry.setDiscount(getDiscount(event, dateTime, user, seats));
        		
        		createdOrderEntries.add(newOrderEntry);
        	}
        }
        
        return orderService.createOrder(user, createdOrderEntries);
    }

	private OrderEntry createOrderEntry() {
		return orderEntryDao.save(new OrderEntry());
	}

	private Set<Ticket> saveTickets(List<Ticket> ticketsToSave, OrderEntry newOrderEntry) {
		return ticketDao.saveTickets(newOrderEntry, ticketsToSave);
	}
    
    private Map<Long, Map<LocalDateTime, List<Ticket>>> groupByEventAndDate(Set<Ticket> tickets) {
		return tickets.stream().collect(Collectors.groupingBy(Ticket::getEventId, 
				Collectors.groupingBy(Ticket::getDateTime)));
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

	public void setOrderEntryDao(OrderEntryDao orderEntryDao) {
		this.orderEntryDao = orderEntryDao;
	}

	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}

	public void setAdditionalPriceStrategy(
			EventAdditionalPriceStrategy additionalPriceStrategy) {
		this.additionalPriceStrategy = additionalPriceStrategy;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

}
