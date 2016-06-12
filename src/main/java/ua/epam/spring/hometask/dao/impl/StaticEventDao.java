package ua.epam.spring.hometask.dao.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.EventDao;
import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OriginalDomainObjectProvider;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

public class StaticEventDao implements EventDao, OriginalDomainObjectProvider<Event> {

    private Set<Event> events = new HashSet<>();
    
    private OriginalDomainObjectProvider<Auditorium> originalAuditoriumProvider;
    private IdGenerator idGenerator;
    
    @Override
    public Event save(Event newEvent) {
        Event savedEvent = null;
        if (canUpdate(newEvent)) {
            savedEvent = updateExistingEvent(newEvent);
        } else {
            savedEvent = createNewEvent(newEvent);
        }
        return getEventCopyWithDependencies(savedEvent);
    }

    private Event createNewEvent(Event newEvent) {
        Event savedEvent;
        savedEvent = new Event(newEvent);
        savedEvent.setId(idGenerator.generateNextId());
        savedEvent.setAuditoriums(getOriginalAuditoriums(newEvent));
        events.add(savedEvent);
        return savedEvent;
    }

    private Event updateExistingEvent(Event newEvent) {
        Event savedEvent;
        savedEvent = getOriginalDomainObject(newEvent);
        savedEvent.setAirDates(newEvent.getAirDates());
        savedEvent.setBasePrice(newEvent.getBasePrice());
        savedEvent.setName(newEvent.getName());
        savedEvent.setRating(newEvent.getRating());
        savedEvent.setAuditoriums(getOriginalAuditoriums(newEvent));
        return savedEvent;
    }
    
    private boolean canUpdate(Event newEvent) {
        Event originalEvent = getOriginalDomainObject(newEvent);
        boolean eventExists = events.contains(newEvent);
        if (originalEvent == null && eventExists) {
            throw new IllegalStateException("event already exists");
        }
        return originalEvent != null;
    }

    @Override
    public void remove(Event object) {
        Event originalEvent = getOriginalDomainObject(object);
        if (originalEvent == null) {
            throw new IllegalStateException("event does not exist");
        }
        events.remove(originalEvent);
    }

    @Override
    public Event getById(long id) {
        return events.stream().filter(e -> e.getId() == id).findFirst()
                .map(this::getEventCopyWithDependencies).orElse(null);
    }
    
    private Event getEventCopyWithDependencies(Event originalEvent) {
        Event copyOfEvent = new Event(originalEvent);
        if (originalEvent.getAuditoriums() != null) {
            NavigableMap<LocalDateTime, Auditorium> copyOfAuditoriums = getCopyOfAuditoriums(originalEvent);
            copyOfEvent.setAuditoriums(copyOfAuditoriums);
        }
        return copyOfEvent;
    }

    private NavigableMap<LocalDateTime, Auditorium> getCopyOfAuditoriums(
            Event originalEvent) {
        return originalEvent.getAuditoriums().entrySet().stream()
            .collect(Collectors.toMap(e -> e.getKey(), 
                        e -> new Auditorium(e.getValue()), 
                        (e1, e2) -> e1, 
                        TreeMap::new));
    }
    
    private NavigableMap<LocalDateTime, Auditorium> getOriginalAuditoriums(Event event) {
        return event.getAuditoriums().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(),
                            e -> originalAuditoriumProvider.getOriginalDomainObject(e.getValue()),
                            (e1, e2) -> e1, 
                            TreeMap::new));
    }
    

    @Override
    public Collection<Event> getAll() {
        return events.stream().map(this::getEventCopyWithDependencies).collect(Collectors.toList());
    }

    @Override
    public Event getByName(String name) {
        return events.stream().filter(e -> Objects.equals(e.getName(), name)).findFirst()
                .map(this::getEventCopyWithDependencies).orElse(null);
    }

    @Override
    public Event getOriginalDomainObject(Event object) {
        return events.stream().filter(e -> e.getId() == object.getId()).findFirst().orElse(null);
    }

}
