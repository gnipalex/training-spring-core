package ua.epam.spring.hometask.dao.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OriginalDomainObjectProvider;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

public class StaticTicketDao implements TicketDao, OriginalDomainObjectProvider<Ticket> {

    private Set<Ticket> tickets = new HashSet<>();
    
    private OriginalDomainObjectProvider<User> originalUserObjectProvider;
    private OriginalDomainObjectProvider<Event> originalEventObjectProvider
    ;
    private EventDao eventDao;
    private UserDao userDao;
    
    private IdGenerator ticketIdGenerator;
    
    @Override
    public Ticket save(Ticket newTicket) {
        Ticket savedTicket = null;
        if (canUpdate(newTicket)) {
            savedTicket = updateExistingTicket(newTicket);
        } else {
            savedTicket = createNewTicket(newTicket);
        }
        return getTicketCopyWithDependencies(savedTicket);
    }

    private Ticket createNewTicket(Ticket newTicket) {
        Ticket savedTicket = new Ticket(newTicket);
        savedTicket.setId(ticketIdGenerator.generateNextId());
        setOriginalEvent(newTicket, savedTicket);
        setOriginalUser(newTicket, savedTicket);
        tickets.add(savedTicket);
        return savedTicket;
    }

    private Ticket updateExistingTicket(Ticket newTicket) {
        Ticket savedTicket = getOriginalDomainObject(newTicket);
        savedTicket.setDateTime(newTicket.getDateTime());
        savedTicket.setSeat(newTicket.getSeat());
        setOriginalEvent(newTicket, savedTicket);
        setOriginalUser(newTicket, savedTicket);
        return savedTicket;
    }
    
    private boolean canUpdate(Ticket newTicket) {
        Ticket originalTicket = getOriginalDomainObject(newTicket);
        boolean ticketExists = tickets.contains(newTicket);
        if (originalTicket == null && ticketExists) {
            throw new IllegalStateException("ticket already exists");
        } 
        return originalTicket != null;
    }

    private void setOriginalUser(Ticket newTicket, Ticket savedTicket) {
        if (newTicket.getUser() != null) {
            User originalUser = originalUserObjectProvider.getOriginalDomainObject(newTicket.getUser());
            if (originalUser == null) {
                throw new IllegalStateException("user not found");
            }
            savedTicket.setUser(originalUser);
        }
    }

    private void setOriginalEvent(Ticket newTicket, Ticket savedTicket) {
        Event originalEvent = originalEventObjectProvider.getOriginalDomainObject(newTicket.getEvent());
        if (originalEvent == null) {
            throw new IllegalStateException("event not found");
        }
        savedTicket.setEvent(originalEvent);
    }

    @Override
    public void remove(Ticket object) {
        Ticket originalTicket = getOriginalDomainObject(object);
        if (originalTicket == null) {
            throw new IllegalStateException("ticket does not exist");
        }
        tickets.remove(originalTicket);
    }

    @Override
    public Ticket getById(long id) {
        Optional<Ticket> ticket = tickets.stream().filter(t -> t.getId() == id).findFirst();
        return ticket.map(this::getTicketCopyWithDependencies).orElse(null);
    }
    
    private Ticket getTicketCopyWithDependencies(Ticket originalTicket) {
        Ticket copyTicket = new Ticket(originalTicket);
        if (originalTicket.getEvent() != null) {
            Event eventCopy = eventDao.getById(originalTicket.getEvent().getId());
            copyTicket.setEvent(eventCopy);
        }
        if (originalTicket.getUser() != null) {
            User userCopy = userDao.getById(originalTicket.getUser().getId());
            copyTicket.setUser(userCopy);
        }
        return copyTicket;
    }

    @Override
    public Collection<Ticket> getAll() {
        return tickets.stream().map(this::getTicketCopyWithDependencies).collect(Collectors.toList());
    }

    @Override
    public Set<Ticket> getTicketsForUser(User user) {
        return tickets.stream().filter(t -> Objects.equals(t.getUser(), user)).map(this::getTicketCopyWithDependencies).collect(Collectors.toSet());
    }
    
    @Override
    public Set<Ticket> getTicketsForEventAndDateTime(Event event, LocalDateTime dateTime) {
        return tickets.stream().filter(t -> ticketsForSameEvent(t, event))
            .filter(t -> ticketsForSameDate(t, dateTime))
            .map(this::getTicketCopyWithDependencies).collect(Collectors.toSet());
    }
    
    private boolean ticketsForSameEvent(Ticket ticket, Event event) {
        return ticket.getEvent().equals(event);
    }
    
    private boolean ticketsForSameDate(Ticket ticket, LocalDateTime dateTime) {
        return ticket.getDateTime().equals(dateTime);
    }

    @Override
    public Ticket getOriginalDomainObject(Ticket object) {
        return tickets.stream().filter(t -> t.getId() == object.getId()).findFirst().orElse(null);
    }

    @Override
    public boolean doesBookingExist(Ticket ticket) {
        return tickets.contains(ticket);
    }

}
