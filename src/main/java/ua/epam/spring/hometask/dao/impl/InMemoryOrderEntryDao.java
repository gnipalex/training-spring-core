package ua.epam.spring.hometask.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;

public class InMemoryOrderEntryDao implements OrderEntryDao {

	private List<OrderEntry> entries = new ArrayList<>();
	
	private TicketDao ticketDao;
	private IdGenerator idGenerator;
	
	@Override
	public OrderEntry save(OrderEntry orderEntry) {
		OrderEntry savedEntry = null;
		if (orderEntryExists(orderEntry)) {
			savedEntry = getOriginalOrderEntryById(orderEntry.getId()).get();
		} else {
			savedEntry = new OrderEntry();
			savedEntry.setId(idGenerator.generateNextId());
			entries.add(savedEntry);
		}
		savedEntry.setBasePrice(orderEntry.getBasePrice());
		savedEntry.setDiscount(orderEntry.getDiscount());
		savedEntry.setOrderId(orderEntry.getOrderId());
		return getOrderEntryCopy(savedEntry);
	}

	private boolean orderEntryExists(OrderEntry orderEntry) {
		return getOriginalOrderEntryById(orderEntry.getId()).isPresent();
	}
	
	private Optional<OrderEntry> getOriginalOrderEntryById(long id) {
		return entries.stream().filter(oe -> oe.getId() == id).findFirst().map(this::getOrderEntryCopy);
	}
	
	private OrderEntry getOrderEntryCopy(OrderEntry orderEntry) {
		return new OrderEntry(orderEntry);
	}

	@Override
	public void remove(OrderEntry object) {
		if (!orderEntryExists(object)) {
			throw new IllegalArgumentException("order entry does not exist");
		}
		entries.remove(object);
	}
	

	@Override
	public OrderEntry getById(long id) {
		return getOriginalOrderEntryById(id).map(this::getOrderEntryCopy).orElse(null);
	}

	@Override
	public Collection<OrderEntry> getAll() {
		return entries.stream().map(this::getOrderEntryCopy)
				.map(this::getOrderEntryCopy).collect(Collectors.toSet());
	}

	// do I need this ?
	@Override
	public Set<OrderEntry> saveAll(Set<OrderEntry> orderEntry) {
		return null;
	}

	// move this method to OrderDao
	@Override
	public void removeOrderEntriesForOrder(Order order) {
		Set<OrderEntry> orderEntriesForOrder = getOrderEntriesForOrder(order);
		orderEntriesForOrder.forEach(this::removeAllTicketsForOrderEntry);
		orderEntriesForOrder.forEach(this::remove);
	}
	
	@Override
	public Set<OrderEntry> getOrderEntriesForOrder(Order order) {
		return entries.stream().filter(oe -> Objects.equals(oe.getOrderId(), order.getId()))
				.map(this::getOrderEntryCopy)
				.collect(Collectors.toSet());
	}
	
	private void removeAllTicketsForOrderEntry(OrderEntry orderEntry) {
		Set<Ticket> ticketsForOrderEntry = ticketDao.getTicketsForOrderEntry(orderEntry);
		ticketsForOrderEntry.forEach(ticketDao::remove);
	}
	

}
