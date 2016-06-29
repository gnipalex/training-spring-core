package ua.epam.spring.hometask.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.EventService;

@Aspect
public class CounterAspect {
    
    private static final Logger LOG = Logger.getLogger(CounterAspect.class); 
    
    private Map<String, Long> eventAccessedByNameTimes = new HashMap<>();
    private Map<String, Long> eventPriceQueriedTimes = new HashMap<>();
    private Map<String, Long> eventBookedTicketsForEvent = new HashMap<>();
    
    @Resource
    private EventService eventService;
    
    @Pointcut("execution(* ua.epam.spring.hometask.service.EventService.getByName(*))")
    private void eventAccessByName() {}
    
    @After("eventAccessByName() && args(name)")
    public void countEventAccessByName(String name) {
        if (name != null) {
            Long accessTimes = increment(eventAccessedByNameTimes.get(name));
            eventAccessedByNameTimes.put(name, accessTimes);
            LOG.info(String.format("event [%s] accessed %d times", name, accessTimes));
        }
    }
    
    private Long increment(Long value) {
        long valueToIncrement = Objects.isNull(value) ? 0L : value.longValue();
        return ++valueToIncrement;
    }
    
    @Pointcut("execution(* ua.epam.spring.hometask.service.BookingService.getTicketsPrice(..))")
    private void eventPriceQuerried() {}
    
    @Before("eventPriceQuerried() && args(event,..)")
    public void countEventPriceQuery(Event event) {
        String eventName = event.getName();
        Long accessTimes = increment(eventPriceQueriedTimes.get(eventName));
        eventPriceQueriedTimes.put(eventName, accessTimes);
        LOG.info(String.format("price was queried %d times for event [%s]", accessTimes, eventName));
    }
    
    @Pointcut("execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    private void bookTickets() {}

    @AfterReturning("bookTickets() && args(*,tickets)")
    public void countBookedTicketsForEvent(Set<Ticket> tickets) {
        List<Event> eventsForBookedTickets = tickets.stream().map(t -> eventService.getById(t.getEventId())).collect(Collectors.toList());
        eventsForBookedTickets.forEach(e -> {
            String eventName = e.getName();
            Long bookingTimes = increment(eventBookedTicketsForEvent.get(eventName));
            eventBookedTicketsForEvent.put(eventName, bookingTimes);
            LOG.info(String.format("ticket for event [%s] was booked %d times", eventName, bookingTimes));
        });
    }
 
}
