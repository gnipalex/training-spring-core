package ua.epam.spring.hometask.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import ua.epam.spring.hometask.dao.AuditoriumDao;
import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

//@Repository("eventDao")
public class InMemoryEventDao implements EventDao {

    private List<Event> events = new ArrayList<>();

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private AuditoriumDao auditoriumDao;
    @Resource
    private TicketDao ticketDao;

    @Override
    public Event save(Event event) {
        assertEventIsUnique(event);
        Event savedEvent = null;
        if (canUpdate(event)) {
            savedEvent = getOriginalEvent(event).get();
        } else {
            savedEvent = new Event();
            savedEvent.setId(idGenerator.generateNextId());
            events.add(savedEvent);
        }
        savedEvent.setAirDates(event.getAirDates());
        savedEvent.setAuditoriums(event.getAuditoriums());
        savedEvent.setBasePrice(event.getBasePrice());
        savedEvent.setName(event.getName());
        savedEvent.setRating(event.getRating());

        return getEventCopy(savedEvent);
    }

    private void assertEventIsUnique(Event event) {
        boolean idsAreSame = getOriginalEvent(event).isPresent();
        if (eventExists(event) && !idsAreSame) {
            throw new IllegalArgumentException("such event is not unique");
        }
    }

    private boolean eventExists(Event event) {
        return events.contains(event);
    }

    private Event getEventCopy(Event event) {
        return new Event(event);
    }

    private Optional<Event> getOriginalEvent(Event event) {
        return events.stream().filter(e -> e.getId() == event.getId())
                .findFirst();
    }

    private boolean canUpdate(Event event) {
        return getOriginalEvent(event).isPresent();
    }

    @Override
    public void remove(Event event) {
        if (!canUpdate(event)) {
            throw new IllegalArgumentException("event doesn't exist");
        }
        if (ticketsPresentForEvent(event)) {
            throw new IllegalStateException(
                    "event can not be deleted as there are tickets for this event");
        }
        events.remove(event);
    }

    private boolean ticketsPresentForEvent(Event event) {
        return CollectionUtils.isNotEmpty(ticketDao.getTicketsForEvent(event));
    }

    @Override
    public Event getById(long id) {
        return events.stream().filter(e -> e.getId() == id).findFirst()
                .map(this::getEventCopy).orElse(null);
    }

    @Override
    public Collection<Event> getAll() {
        return events.stream().map(this::getEventCopy)
                .collect(Collectors.toList());
    }

    @Override
    public Event getByName(String name) {
        return events.stream().filter(e -> Objects.equals(e.getName(), name))
                .findFirst().map(this::getEventCopy).orElse(null);
    }

    @Override
    public NavigableMap<LocalDateTime, Auditorium> getAuditoriumAssignments(
            Event event) {
        return event
                .getAuditoriums()
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(e -> e.getKey(), e -> auditoriumDao
                                .getByCode(e.getValue()), (e1, e2) -> e1,
                                TreeMap::new));
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void setAuditoriumDao(AuditoriumDao auditoriumDao) {
        this.auditoriumDao = auditoriumDao;
    }

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
