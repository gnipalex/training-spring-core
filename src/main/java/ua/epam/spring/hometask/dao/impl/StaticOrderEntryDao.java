package ua.epam.spring.hometask.dao.impl;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.dao.OriginalDomainObjectProvider;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;

public class StaticOrderEntryDao implements OrderEntryDao,
        OriginalDomainObjectProvider<OrderEntry> {

    private Set<OrderEntry> orderEntries = new HashSet<>();

    private TicketDao ticketDao;
    
    private Set<SimpleEntry<Long, Long>> orderEntryToTicketsRelation = new HashSet<>();

    @Override
    public OrderEntry save(OrderEntry orderEntry) {
    	if (orderEntries.contains(orderEntry)) {
    		orderEntry.getTickets();
    	}
        return null;
    }
    
    private Set<Ticket> getOriginalTickets

    @Override
    public void remove(OrderEntry object) {
        // TODO Auto-generated method stub

    }

    @Override
    public OrderEntry getById(long id) {
        return orderEntries.stream().filter(oe -> oe.getId() == id).findFirst()
                .map(this::getOrderEntryCopyWithDependencies).orElse(null);
    }

    private OrderEntry getOrderEntryCopyWithDependencies(OrderEntry orderEntry) {
        OrderEntry copyOrderEntry = new OrderEntry(orderEntry);
        Set<Ticket> copyOfTickets = getTicketsCopy(orderEntry);
        copyOrderEntry.setTickets(copyOfTickets);
        return copyOrderEntry;
    }

    private Set<Ticket> getTicketsCopy(OrderEntry orderEntry) {
        return orderEntry.getTickets().stream()
                .map(t -> ticketDao.getById(t.getId()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<OrderEntry> getAll() {
        return orderEntries.stream().map(this::getOrderEntryCopyWithDependencies).collect(Collectors.toSet());
    }

    @Override
    public OrderEntry getOriginalDomainObject(OrderEntry object) {
        return orderEntries.stream().filter(oe -> oe.getId() == object.getId())
                .findFirst().orElse(null);
    }

    @Override
    public Set<OrderEntry> saveAll(Set<OrderEntry> orderEntry) {
        return orderEntry.stream().map(this::save).collect(Collectors.toSet());
    }

}
