package ua.epam.spring.hometask.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.OrderService;
import ua.epam.spring.hometask.service.UserService;

//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DefaultBookingServiceIntegrationTest extends AbstractServiceIntegrationTest {

    private static final String USER_1_EMAIL = "user 1 email";
    private static final String EVENT_1_NAME = "event 1 name";
    private static final double EVENT_1_BASE_PRICE = 5D;
    private static final double EVENT_1_HIGH_RATED_PRICE = EVENT_1_BASE_PRICE * 1.2;
    private static final double EVENT_1_BIRTHDAY_DISCOUNT = EVENT_1_BASE_PRICE * 0.05;
    private static final LocalDateTime EVENT_1_DATE_1  = LocalDateTime.of(2016, 4, 5, 0, 1);
    
    private static final LocalDateTime USER_BIRTHDAY = LocalDateTime.of(1990, 4, 9, 0, 0);
    
    private static final String EVENT_2_NAME = "event 2 name";
    private static final double EVENT_2_BASE_PRICE = 7D;
    private static final LocalDateTime EVENT_2_DATE_1  = LocalDateTime.of(2016, 3, 10, 0, 0);
    private static final String AUDITORIUM_CODE = "test-auditorium-1";
    
    @Resource
    private OrderService orderService;
    @Resource
    private UserService userService;
    @Resource
    private EventService eventService;
    @Resource
    private AuditoriumService auditoriumService;
    @Resource
    private BookingService bookingService;
    @Resource
    private TicketDao ticketDao;
    
    private Event event1;
    private Event event2;
    private User user;
    
    @Before
    public void before() {
        this.user = saveUser();
        this.event1 = saveEvent(EVENT_1_NAME, EVENT_1_DATE_1, AUDITORIUM_CODE, EVENT_1_BASE_PRICE);
        this.event2 = saveEvent(EVENT_2_NAME, EVENT_2_DATE_1, AUDITORIUM_CODE, EVENT_2_BASE_PRICE);
    }

    private User saveUser() {
        User user = new User(); 
        user.setBalance(25);
        user.setBirthday(USER_BIRTHDAY);
        user.setEmail(USER_1_EMAIL);
        user.setFirstName("first name");
        user.setLastName("last name");
        return userService.save(user);
    }
    
    private Event saveEvent(String name, LocalDateTime dateTime, String auditoriumCode, double basePrice) {
        Event event = new Event();
        Auditorium auditorium = auditoriumService.getByCode(auditoriumCode);
        event.addAirDateTime(dateTime, auditorium);
        event.setBasePrice(basePrice);
        event.setName(name);
        event.setRating(EventRating.MID);
        return eventService.save(event);
    }
    
    @Test
    public void shouldCreateBooking() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 1);
        Ticket ticket2 = prepareTicket(EVENT_1_DATE_1, event1, 2);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        
        Order order = bookingService.bookTickets(user, tickets);
        assertThat(order).isNotNull();
    }
    
    @Test
    public void shouldSetUserToCreatedBooking() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 1);
        Ticket ticket2 = prepareTicket(EVENT_1_DATE_1, event1, 2);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        
        Order order = bookingService.bookTickets(user, tickets);
        assertThat(order.getUserId()).isEqualTo(user.getId());
    }

    private Ticket prepareTicket(LocalDateTime dateTime, Event event, long seat) {
        Ticket ticket = new Ticket();
        ticket.setDateTime(dateTime);
        ticket.setEventId(event.getId());
        ticket.setSeat(seat);
        return ticket;
    }
    
    @Test
    public void shouldCreateOneOrderEntry_whenTicketsHaveSameDateAndEvent() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 1);
        Ticket ticket2 = prepareTicket(EVENT_1_DATE_1, event1, 2);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        Order order = bookingService.bookTickets(user, tickets);
        
        Set<OrderEntry> orderEntries = orderService.getOrderEntries(order);
        assertThat(orderEntries).hasSize(1);
    }
    
    @Test
    public void shouldCreateTwoOrderEntries_whenTicketsHaveDifferentDates() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 1);
        Ticket ticket2 = prepareTicket(EVENT_2_DATE_1, event2, 2);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        Order order = bookingService.bookTickets(user, tickets);
        
        Set<OrderEntry> orderEntries = orderService.getOrderEntries(order);
        assertThat(orderEntries).hasSize(2);
    }

    private Set<Ticket> toSet(Ticket... tickets) {
        return Arrays.stream(tickets).collect(Collectors.toSet());
    }
    
    @Test
    public void shouldSetBasePriceToOrderEntries_whenEachOrderEntryHasOneTicket() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 10);
        Ticket ticket2 = prepareTicket(EVENT_2_DATE_1, event2, 11);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        Order order = bookingService.bookTickets(user, tickets);
        Set<OrderEntry> orderEntries = orderService.getOrderEntries(order);
        
        OrderEntry orderEntry1 = getByEventAndDate(orderEntries, event1, EVENT_1_DATE_1);
        assertThat(orderEntry1.getBasePrice()).isEqualByComparingTo(EVENT_1_BASE_PRICE);
        
        OrderEntry orderEntry2 = getByEventAndDate(orderEntries, event2, EVENT_2_DATE_1);
        assertThat(orderEntry2.getBasePrice()).isEqualByComparingTo(EVENT_2_BASE_PRICE);
    }
    
    @Test
    public void shouldSetHighBasePriceToFirstOrderEntry_whenFirstEventIsHighRated() {
        event1.setRating(EventRating.HIGH);
        eventService.save(event1);
        
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 10);
        Ticket ticket2 = prepareTicket(EVENT_2_DATE_1, event2, 11);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        Order order = bookingService.bookTickets(user, tickets);
        Set<OrderEntry> orderEntries = orderService.getOrderEntries(order);
        
        OrderEntry orderEntry1 = getByEventAndDate(orderEntries, event1, EVENT_1_DATE_1);
        assertThat(orderEntry1.getBasePrice()).isEqualByComparingTo(EVENT_1_HIGH_RATED_PRICE);
    }
    
    private OrderEntry getByEventAndDate(Set<OrderEntry> orderEntries, Event event, LocalDateTime dateTime) {
        return orderEntries.stream().filter(oe -> hasSameEventAndDate(oe, event, dateTime))
                .findFirst().get();
    }
    
    private boolean hasSameEventAndDate(OrderEntry orderEntry, Event event, LocalDateTime dateTime) {
        return ticketDao.getTicketsForOrderEntry(orderEntry).stream()
                .anyMatch(t -> t.getEventId() == event.getId() && 
                    Objects.equals(t.getDateTime(), dateTime));
    }
    
    @Test
    public void shouldApplyDiscountToFirstOrderEntry_whenUserHasBirthDayAfterFourDays() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 10);
        Ticket ticket2 = prepareTicket(EVENT_2_DATE_1, event2, 11);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        Order order = bookingService.bookTickets(user, tickets);
        Set<OrderEntry> orderEntries = orderService.getOrderEntries(order);
        
        OrderEntry orderEntry1 = getByEventAndDate(orderEntries, event1, EVENT_1_DATE_1);
        assertThat(orderEntry1.getDiscount()).isEqualByComparingTo(EVENT_1_BIRTHDAY_DISCOUNT);
        
        OrderEntry orderEntry2 = getByEventAndDate(orderEntries, event2, EVENT_2_DATE_1);
        assertThat(orderEntry2.getDiscount()).isEqualByComparingTo(0D);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformBookTicketsAndTicketIsAlreadyBooked() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 10);
        Ticket ticket2 = prepareTicket(EVENT_2_DATE_1, event2, 11);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        Order order = bookingService.bookTickets(user, tickets);
        
        Ticket ticket3 = prepareTicket(EVENT_1_DATE_1, event1, 10);
        Set<Ticket> additionalTickets = toSet(ticket3);
        bookingService.bookTickets(user, additionalTickets);
    }
    
    @Test
    public void shouldReturnPriceForEvent() {
        double price = bookingService.getTicketsPrice(event1, EVENT_1_DATE_1, user, new HashSet<>(Arrays.<Long> asList(1L)));
        assertThat(price).isGreaterThan(0D);
    }
    
    @Test
    public void shouldCreateBooking_whenUserIsNull() {
        Ticket ticket1 = prepareTicket(EVENT_1_DATE_1, event1, 1);
        Ticket ticket2 = prepareTicket(EVENT_1_DATE_1, event1, 2);
        Set<Ticket> tickets = toSet(ticket1, ticket2);
        
        Order order = bookingService.bookTickets(null, tickets);
        assertThat(order).isNotNull();
    }
    
    

}
