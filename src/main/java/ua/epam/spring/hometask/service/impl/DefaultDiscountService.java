package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

public class DefaultDiscountService implements DiscountService {

    private List<DiscountStrategy> discountStrategies = Collections.emptyList();
    
    private OrderDao orderDao;
    private TicketDao ticketDao;
    private EventDao eventDao;
    private UserDao userDao;
    
    @Override
    public double getDiscount(User user, Event event, LocalDateTime airDateTime, long numberOfTickets) {
        List<Double> discounts = evaluateDiscounts(user, event, airDateTime, numberOfTickets);
        return getMaxDiscount(discounts);
    }
    
    private double getMaxDiscount(List<Double> discounts) {
        return discounts.stream().max(Double::compare).get();
    }
    
    private List<Double> evaluateDiscounts(User user, Event event, LocalDateTime airDateTime,
            long numberOfTickets) {
        return discountStrategies.stream()
            .map(strategy -> strategy.getDiscount(user, event, airDateTime, numberOfTickets))
            .collect(Collectors.toList());
    }

	@Override
	public double getDiscount(OrderEntry orderEntry) {
		Map<Long, Map<LocalDateTime, List<Ticket>>> ticketsGroupedByEventIdAndDate = getTicketsGroupedByEventAndAirDate(orderEntry);

		double totalOrderEntryDiscount = 0.0D;
		
		User user = getUserForOrderEntry(orderEntry);
		
		for (Entry<Long, Map<LocalDateTime, List<Ticket>>> eventEntry : ticketsGroupedByEventIdAndDate.entrySet()) {
			Event event = eventDao.getById(eventEntry.getKey());
			
			Map<LocalDateTime, List<Ticket>> ticketsGroupedByDate = eventEntry.getValue();
			
			for (Entry<LocalDateTime, List<Ticket>> dateEntry : ticketsGroupedByDate.entrySet()) {
				LocalDateTime dateTime = dateEntry.getKey();
				long ticketsCount = dateEntry.getValue().size();
				
				totalOrderEntryDiscount += getDiscount(user, event, dateTime, ticketsCount);
			}
		}
		
		return totalOrderEntryDiscount;
	}
	
	private Map<Long, Map<LocalDateTime, List<Ticket>>> getTicketsGroupedByEventAndAirDate(OrderEntry orderEntry) {
		Set<Ticket> tickets = getTicketsForOrderEntry(orderEntry);
		return groupByEventAndDate(tickets);
	}
	
	private Set<Ticket> getTicketsForOrderEntry(OrderEntry orderEntry) {
		return ticketDao.getTicketsForOrderEntry(orderEntry);
	}
	
	private Map<Long, Map<LocalDateTime, List<Ticket>>> groupByEventAndDate(Set<Ticket> tickets) {
		return tickets.stream().collect(Collectors.groupingBy(Ticket::getEventId, 
				Collectors.groupingBy(Ticket::getDateTime)));
	}
	
	// move to user dao
	private User getUserForOrderEntry(OrderEntry orderEntry) {
		Order order = orderDao.getById(orderEntry.getOrderId());
		return userDao.getById(order.getUserId());
	}
	
    public void setDiscountStrategies(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }
    
}
