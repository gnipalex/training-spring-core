package ua.epam.spring.hometask.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

public class InMemoryTicketDao implements TicketDao {

	private List<Ticket> tickets = new ArrayList<>();
	
	private IdGenerator idGenerator;
	private OrderEntryDao orderEntryDao;
	private OrderDao orderDao;
	
	@Override
	public Ticket save(Ticket ticket) {
		Ticket savedTicket = null;
		if (ticketExists(ticket)) {
			savedTicket = getOriginalTicket(ticket.getId()).get();
		} else {
			savedTicket = new Ticket();
			savedTicket.setId(idGenerator.generateNextId());
			tickets.add(savedTicket);
		}
		savedTicket.setDateTime(ticket.getDateTime());
		savedTicket.setSeat(ticket.getSeat());
		savedTicket.setEventId(ticket.getEventId());
//		savedTicket.setUserId(ticket.getUserId());
		savedTicket.setOrderEntryId(ticket.getOrderEntryId());
		return getTicketCopy(savedTicket);
	}

	private Ticket getTicketCopy(Ticket ticket) {
		return new Ticket(ticket);
	}

	private Optional<Ticket> getOriginalTicket(long id) {
		return tickets.stream().filter(t -> t.getId() == id).findFirst();
	}

	private boolean ticketExists(Ticket ticket) {
		return getOriginalTicket(ticket.getId()).isPresent();
	}

	@Override
	public void remove(Ticket ticket) {
		if (!ticketExists(ticket)) {
			throw new IllegalArgumentException("ticket does not exist");
		}
		// check equals
		tickets.remove(ticket);
	}

	@Override
	public Ticket getById(long id) {
		return getOriginalTicket(id).map(this::getTicketCopy).orElse(null);
	}

	@Override
	public Collection<Ticket> getAll() {
		return tickets.stream().map(this::getTicketCopy).collect(Collectors.toList());
	}

	@Override
	public Set<Ticket> getTicketsForUser(User user) {
		Set<Order> allOrdersForUser = orderDao.getOrdersForUser(user);
		return allOrdersForUser.stream()
				.map(orderEntryDao::getOrderEntriesForOrder)
				.flatMap(Set::stream).map(this::getTicketsForOrderEntry)
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<Ticket> getTicketsForEventAndDateTime(Event event, LocalDateTime dateTime) {
		return tickets.stream().filter(t -> ticketsForEvent(t, event))
	            .filter(t -> ticketsForDate(t, dateTime))
	            .map(this::getTicketCopy)
	            .collect(Collectors.toSet());
	}
	
    private boolean ticketsForEvent(Ticket ticket, Event event) {
        return ticket.getEventId() == event.getId();
    }
    
    private boolean ticketsForDate(Ticket ticket, LocalDateTime dateTime) {
        return Objects.equals(ticket.getDateTime(), dateTime);
    }

//	@Override
//	public boolean doesBookingExist(Ticket ticket) {
//		return false;
//	}

	@Override
	public Set<Ticket> getTicketsForOrderEntry(OrderEntry orderEntry) {
		return null;
	}

}
