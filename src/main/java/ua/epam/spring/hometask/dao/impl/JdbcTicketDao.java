package ua.epam.spring.hometask.dao.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.dao.util.DaoUtils;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

@Repository("ticketDao")
public class JdbcTicketDao extends AbstractJdbcDao<Ticket> implements TicketDao {

    private static final String SELECT_BY_ID = "SELECT * FROM tickets WHERE id=?";
    private static final String SELECT_ALL = "SELECT * FROM tickets";
    private static final String INSERT_TICKET = "INSERT INTO tickets(dateTime,seat,orderEntryId,eventId) VALUES(?,?,?,?)"; 
    private static final String UPDATE_TICKET_BY_ID = "UPDATE tickets SET dateTime=?, seat=?, orderEntryId=?, eventId=? WHERE id=?";
    private static final String REMOVE_BY_ID = "DELETE FROM tickets WHERE id=?";
    private static final String SELECT_BY_USER_ID = 
            "SELECT t.id id, t.dateTime dateTime, t.seat seat, t.orderEntryId orderEntryId, t.eventId eventId"
            + " FROM tickets t"
            + " JOIN orderEntries oe ON oe.id = t.orderEntryId"
            + " JOIN orders o ON o.id = oe.orderId"
            + " WHERE o.userId=?";
    private static final String SELECT_BY_EVENT_AND_DATETIME = "SELECT * FROM tickets"
            + " WHERE dateTime=? AND eventId=?";
    private static final String SELECT_BY_EVENT = "SELECT * FROM tickets WHERE eventId=?";
    private static final String SELECT_BY_EVENT_AND_DATETIME_AND_SEAT = 
            "SELECT * FROM tickets"
            + " WHERE dateTime=? AND eventId=? AND seat=?";
    private static final String SELECT_BY_ORDER_ENTRY = 
            "SELECT t.id id, t.dateTime dateTime, t.seat seat, t.orderEntryId orderEntryId, t.eventId eventId"
            + " FROM tickets t"
            + " JOIN orderEntries oe ON oe.id = t.orderEntryId AND oe.id=?";
     
    @Override
    public Ticket save(Ticket ticket) {
        if (canUpdate(ticket)) {
            updateExistingTicket(ticket);
        } else {
            saveNewTicket(ticket);
        }
        return ticket;
    }

    private void saveNewTicket(Ticket ticket) {
        Object[] args = new Object[] { DaoUtils.toTimestamp(ticket.getDateTime()), 
                ticket.getSeat(), ticket.getOrderEntryId(), 
                ticket.getEventId()};
        long generatedId = updateAndGetKey(INSERT_TICKET, args).longValue();
        ticket.setId(generatedId);
    }

    private void updateExistingTicket(Ticket ticket) {
        Object[] args = new Object[] { DaoUtils.toTimestamp(ticket.getDateTime()), 
                ticket.getSeat(), ticket.getOrderEntryId(), 
                ticket.getEventId(), ticket.getId() };
        updateRow(UPDATE_TICKET_BY_ID, args);
    }

    private boolean canUpdate(Ticket ticket) {
        return getById(ticket.getId()) != null;
    }

    @Override
    public void remove(Ticket ticket) {
        getJdbcTemplate().update(REMOVE_BY_ID, ticket.getId());
    }

    @Override
    public Ticket getById(long id) {
        return queryForObject(SELECT_BY_ID, id);
    }

    @Override
    public Collection<Ticket> getAll() {
        return queryForList(SELECT_ALL);
    }

    @Override
    public Set<Ticket> getTicketsForUser(User user) {
        List<Ticket> tickets = queryForList(SELECT_BY_USER_ID, user.getId());
        return toSet(tickets);
    }

    @Override
    public Set<Ticket> getTicketsForEventAndDateTime(Event event,
            LocalDateTime dateTime) {
        List<Ticket> tickets = queryForList(SELECT_BY_EVENT_AND_DATETIME, 
                DaoUtils.toTimestamp(dateTime), event.getId());
        return toSet(tickets);
    }

    @Override
    public Set<Ticket> getTicketsForEvent(Event event) {
        List<Ticket> tickets = queryForList(SELECT_BY_EVENT, event.getId());
        return toSet(tickets);
    }

    @Override
    public boolean doesBookingExist(Ticket ticket) {
        Ticket existingTicket = queryForObject(SELECT_BY_EVENT_AND_DATETIME_AND_SEAT,
                DaoUtils.toTimestamp(ticket.getDateTime()), ticket.getEventId(), ticket.getSeat());
        return existingTicket != null;
    }

    @Override
    public Set<Ticket> getTicketsForOrderEntry(OrderEntry orderEntry) {
        List<Ticket> tickets = queryForList(SELECT_BY_ORDER_ENTRY, orderEntry.getId());
        return toSet(tickets);
    }
    
    private <T> Set<T> toSet(List<T> list) {
        return list.stream().collect(Collectors.toSet());
    }

    @Override
    public Set<Ticket> saveTickets(OrderEntry orderEntry,
            Collection<Ticket> tickets) {
        tickets.stream().forEach(t -> t.setOrderEntryId(orderEntry.getId()));
        return tickets.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    protected RowMapper<Ticket> getRowMapper() {
        return (resultSet, rowNumber) -> {
            Ticket ticket = new Ticket();
            ticket.setDateTime(DaoUtils.toLocalDateTime(resultSet.getTimestamp("dateTime")));
            ticket.setEventId(resultSet.getLong("eventId"));
            ticket.setId(resultSet.getLong("id"));
            ticket.setOrderEntryId(resultSet.getLong("orderEntryId"));
            ticket.setSeat(resultSet.getLong("seat"));
            return ticket;
        };
    }

}
