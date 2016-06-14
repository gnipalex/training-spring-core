package ua.epam.spring.hometask.service;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;

/**
 * @author Yuriy_Tkach
 */
public interface EventService extends AbstractDomainObjectService<Event> {

    /**
     * Finding event by name
     * 
     * @param name
     *            Name of the event
     * @return found event or <code>null</code>
     */
    @Nullable Event getByName(@Nonnull String name);

    /*
     * Finding all events that air on specified date range
     * 
     * @param from Start date
     * 
     * @param to End date inclusive
     * 
     * @return Set of events
     */
//     @Nonnull Set<Event> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to);

    /*
     * Return events from 'now' till the the specified date time
     * 
     * @param to End date time inclusive
     * s
     * @return Set of events
     */
//     @Nonnull Set<Event> getNextEvents(@Nonnull LocalDateTime to);
    
      
    void assignAuditorium(Event event, Auditorium auditorium, LocalDateTime dateTime);
    
    boolean removeAuditoriumAssignment(Event event, LocalDateTime dateTime);
    
    boolean removeAirDateTime(Event event, LocalDateTime dateTime);

}
